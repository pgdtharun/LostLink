<?php
// seed_data.php - insert mock data into the database with SVG icon mapping
require_once __DIR__ . '/includes/db.php';

// Add `icon` column if not exists
$hasIconColumn = false;
$result = $conn->query("SHOW COLUMNS FROM items LIKE 'icon'");
if ($result && $result->num_rows > 0) {
    $hasIconColumn = true;
}

if (!$hasIconColumn) {
    $conn->query("ALTER TABLE items ADD COLUMN icon VARCHAR(50) DEFAULT 'bag'");
}

// Ensure there is at least one default user
$defaultEmail = 'demo@lostlink.local';
$defaultName = 'Demo User';
$defaultPassword = 'password';

$stmt = $conn->prepare('SELECT id FROM users WHERE email = ? LIMIT 1');
$stmt->bind_param('s', $defaultEmail);
$stmt->execute();
$stmt->store_result();
if ($stmt->num_rows === 0) {
    $hashed = password_hash($defaultPassword, PASSWORD_DEFAULT);
    $insert = $conn->prepare('INSERT INTO users (name, email, password, avatar) VALUES (?, ?, ?, ?)');
    $avatar = 'DU';
    $insert->bind_param('ssss', $defaultName, $defaultEmail, $hashed, $avatar);
    $insert->execute();
    $userId = $insert->insert_id;
    $insert->close();
} else {
    $stmt->bind_result($userId);
    $stmt->fetch();
}
$stmt->close();

// Insert mock items if table currently empty
$countResult = $conn->query('SELECT COUNT(*) as cnt FROM items');
$countRow = $countResult->fetch_assoc();
if (intval($countRow['cnt']) === 0) {
    $mockItems = [
        ['title'=>'Black Backpack', 'description'=>'Lost black hiking backpack near campus library', 'category'=>'Bags & Wallets', 'status'=>'lost', 'location'=>'Library', 'icon'=>'bag'],
        ['title'=>'iPhone 13 Pro', 'description'=>'Found a phone with cracked back near cafeteria', 'category'=>'Electronics', 'status'=>'found', 'location'=>'Cafeteria', 'icon'=>'phone'],
        ['title'=>'Student ID Card', 'description'=>'White student id card found at gate 3', 'category'=>'ID & Cards', 'status'=>'found', 'location'=>'Main Gate', 'icon'=>'document'],
        ['title'=>'Silver Necklace', 'description'=>'Lost favorite necklace at gym locker', 'category'=>'Jewelry', 'status'=>'lost', 'location'=>'Gym', 'icon'=>'jewelry'],
        ['title'=>'Calculus Textbook', 'description'=>'Found calculus textbook in lecture hall 2', 'category'=>'Books & Notes', 'status'=>'found', 'location'=>'Lecture Hall 2', 'icon'=>'book'],
    ];

    $ins = $conn->prepare('INSERT INTO items (user_id, title, description, category, status, location, image, icon) VALUES (?, ?, ?, ?, ?, ?, ?, ?)');
    foreach ($mockItems as $item) {
        $image = null;
        $ins->bind_param('isssssss', $userId, $item['title'], $item['description'], $item['category'], $item['status'], $item['location'], $image, $item['icon']);
        $ins->execute();
    }
    $ins->close();
    echo "Inserted mock items into the database\n";
} else {
    echo "Items already exist in the database (no mock insert performed).\n";
}

echo "Seed process completed.\n";
