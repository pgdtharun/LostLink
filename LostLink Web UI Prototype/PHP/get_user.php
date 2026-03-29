<?php
// get_user.php - Fetch user profile info
require_once __DIR__ . '/includes/db.php';

$user_id = intval($_GET['id'] ?? ($_SESSION['user_id'] ?? 0));
$stmt = $conn->prepare('SELECT id, name, email, avatar, created_at FROM users WHERE id = ?');
$stmt->bind_param('i', $user_id);
$stmt->execute();
$result = $stmt->get_result();
$user = $result->fetch_assoc();
// Use $user in your page
?>
