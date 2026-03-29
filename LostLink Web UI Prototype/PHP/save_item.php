<?php
// save_item.php - Save/unsave an item for the user
require_once __DIR__ . '/includes/db.php';
require_once __DIR__ . '/includes/auth.php';

$user_id = $_SESSION['user_id'];
$item_id = intval($_GET['item_id'] ?? 0);
$action = $_GET['action'] ?? 'save';

if ($item_id) {
    if ($action === 'save') {
        $stmt = $conn->prepare('INSERT IGNORE INTO saved_items (user_id, item_id) VALUES (?, ?)');
        $stmt->bind_param('ii', $user_id, $item_id);
        $stmt->execute();
    } elseif ($action === 'unsave') {
        $stmt = $conn->prepare('DELETE FROM saved_items WHERE user_id = ? AND item_id = ?');
        $stmt->bind_param('ii', $user_id, $item_id);
        $stmt->execute();
    }
}
header('Location: saved-items.php');
exit();
