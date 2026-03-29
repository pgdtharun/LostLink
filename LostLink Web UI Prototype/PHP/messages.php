<?php
require_once __DIR__ . '/config/config.php';
require_once __DIR__ . '/includes/auth.php';
require_once __DIR__ . '/includes/db.php';

$user_id = $_SESSION['user_id'];

$stmt = $conn->prepare('SELECT m.*, su.name as sender_name, ru.name as receiver_name, items.title as item_title FROM messages m JOIN users su ON m.sender_id = su.id JOIN users ru ON m.receiver_id = ru.id LEFT JOIN items ON m.item_id = items.id WHERE m.sender_id = ? OR m.receiver_id = ? ORDER BY m.sent_at DESC');
$stmt->bind_param('ii', $user_id, $user_id);
$stmt->execute();
$result = $stmt->get_result();
$messages = $result->fetch_all(MYSQLI_ASSOC);

include_once __DIR__ . '/includes/header.php';
?>
<div class="container" style="padding:24px;">
    <h1>Messages</h1>
    <div class="messages-grid" style="display:flex; flex-direction: column; gap: 12px;">
        <?php if (empty($messages)): ?>
            <div>No messages yet.</div>
        <?php else: ?>
            <?php foreach ($messages as $msg): ?>
                <div class="card" style="padding:16px; border:1px solid #e5e7eb; border-radius:12px;">
                    <p><strong>From:</strong> <?php echo htmlspecialchars($msg['sender_name']); ?> <strong>To:</strong> <?php echo htmlspecialchars($msg['receiver_name']); ?></p>
                    <?php if (!empty($msg['item_title'])): ?>
                        <p><strong>Item:</strong> <?php echo htmlspecialchars($msg['item_title']); ?></p>
                    <?php endif; ?>
                    <p><?php echo nl2br(htmlspecialchars($msg['content'])); ?></p>
                    <p><small><?= htmlspecialchars($msg['sent_at']); ?></small></p>
                </div>
            <?php endforeach; ?>
        <?php endif; ?>
    </div>
</div>
<?php include_once __DIR__ . '/includes/footer.php'; ?>
