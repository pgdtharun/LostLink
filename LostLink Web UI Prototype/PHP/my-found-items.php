<?php
require_once __DIR__ . '/config/config.php';
require_once __DIR__ . '/includes/auth.php';
require_once __DIR__ . '/includes/db.php';

$user_id = $_SESSION['user_id'];
$stmt = $conn->prepare('SELECT * FROM items WHERE user_id = ? AND status = "found" ORDER BY updated_at DESC');
$stmt->bind_param('i', $user_id);
$stmt->execute();
$result = $stmt->get_result();
$items = $result->fetch_all(MYSQLI_ASSOC);

include_once __DIR__ . '/includes/header.php';
?>
<div class="container" style="padding:24px;">
    <h1>My Found Items</h1>
    <div class="items-grid" style="display:grid; grid-template-columns: repeat(auto-fit,minmax(240px,1fr)); gap:16px;">
        <?php if (empty($items)): ?>
            <div class="card">You have no found items reported yet.</div>
        <?php else: ?>
            <?php foreach ($items as $item): ?>
            <div class="card" style="padding:16px; border:1px solid #e5e7eb; border-radius:12px;">
                <h3><?php echo htmlspecialchars($item['title']); ?></h3>
                <p><?php echo htmlspecialchars($item['description']); ?></p>
                <p><strong>Location:</strong> <?php echo htmlspecialchars($item['location']); ?></p>
                <p><strong>Updated:</strong> <?php echo htmlspecialchars($item['updated_at']); ?></p>
                <a href="item-detail.php?id=<?php echo intval($item['id']); ?>" class="btn btn-secondary btn-sm">Details</a>
            </div>
            <?php endforeach; ?>
        <?php endif; ?>
    </div>
</div>
<?php include_once __DIR__ . '/includes/footer.php'; ?>
