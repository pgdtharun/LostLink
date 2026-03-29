<?php
require_once __DIR__ . '/config/config.php';
require_once __DIR__ . '/includes/auth.php';
require_once __DIR__ . '/includes/db.php';

$user_id = $_SESSION['user_id'];
$stmt = $conn->prepare('SELECT items.*, users.name AS owner_name, saved_items.saved_at FROM saved_items JOIN items ON saved_items.item_id = items.id JOIN users ON items.user_id = users.id WHERE saved_items.user_id = ? ORDER BY saved_items.saved_at DESC');
$stmt->bind_param('i', $user_id);
$stmt->execute();
$result = $stmt->get_result();
$items = $result->fetch_all(MYSQLI_ASSOC);

include_once __DIR__ . '/includes/header.php';
?>
<div class="container" style="padding:24px;">
    <h1>Saved Items</h1>
    <div class="items-grid" style="display:grid; grid-template-columns: repeat(auto-fit,minmax(240px,1fr)); gap:16px;">
        <?php if (empty($items)): ?>
            <div class="card">No saved items yet.</div>
        <?php else: ?>
            <?php foreach ($items as $item): ?>
            <div class="card" style="padding:16px; border:1px solid #e5e7eb; border-radius:12px;">
                <h3><?php echo htmlspecialchars($item['title']); ?></h3>
                <p><strong>Category:</strong> <?php echo htmlspecialchars($item['category']); ?></p>
                <p><strong>Location:</strong> <?php echo htmlspecialchars($item['location']); ?></p>
                <p><strong>Status:</strong> <?php echo htmlspecialchars(ucfirst($item['status'])); ?></p>
                <p><strong>Owner:</strong> <?php echo htmlspecialchars($item['owner_name']); ?></p>
                <p><strong>Saved:</strong> <?php echo htmlspecialchars($item['saved_at']); ?></p>
                <a href="item-detail.php?id=<?php echo intval($item['id']); ?>" class="btn btn-secondary btn-sm">View</a>
                <a href="save_item.php?action=unsave&item_id=<?php echo intval($item['id']); ?>" class="btn btn-ghost btn-sm">Unsave</a>
            </div>
            <?php endforeach; ?>
        <?php endif; ?>
    </div>
</div>
<?php include_once __DIR__ . '/includes/footer.php'; ?>
