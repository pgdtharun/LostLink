<?php
// send_message.php - Send a message to another user
require_once __DIR__ . '/includes/db.php';
require_once __DIR__ . '/includes/auth.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $receiver_id = intval($_POST['receiver_id'] ?? 0);
    $item_id = intval($_POST['item_id'] ?? 0);
    $content = trim($_POST['content'] ?? '');
    $sender_id = $_SESSION['user_id'];

    if ($receiver_id && $content) {
        $stmt = $conn->prepare('INSERT INTO messages (sender_id, receiver_id, item_id, content) VALUES (?, ?, ?, ?)');
        $stmt->bind_param('iiis', $sender_id, $receiver_id, $item_id, $content);
        $stmt->execute();
        // Optionally redirect or show success
    }
}
// Optionally redirect or show error
