<?php
// item_submit.php - Handle lost/found item submission
require_once __DIR__ . '/includes/db.php';
require_once __DIR__ . '/includes/auth.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $title = trim($_POST['title'] ?? '');
    $description = trim($_POST['description'] ?? '');
    $category = trim($_POST['category'] ?? '');
    $status = trim($_POST['status'] ?? 'lost');
    $location = trim($_POST['location'] ?? '');
    $user_id = $_SESSION['user_id'];
    $image = null; // Handle file upload if needed

    if ($title && $category && $location) {
        $stmt = $conn->prepare('INSERT INTO items (user_id, title, description, category, status, location, image) VALUES (?, ?, ?, ?, ?, ?, ?)');
        $stmt->bind_param('issssss', $user_id, $title, $description, $category, $status, $location, $image);
        if ($stmt->execute()) {
            header('Location: dashboard.php?item=added');
            exit();
        } else {
            $error = 'Failed to submit item: ' . $conn->error;
        }
    } else {
        $error = 'Title, category, and location are required.';
    }
}
?>
<!-- Item submission form HTML here -->
