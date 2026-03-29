<?php
require_once __DIR__ . '/config/config.php';
require_once __DIR__ . '/includes/auth.php';
require_once __DIR__ . '/includes/db.php';
require_once __DIR__ . '/get_items.php';

$search = trim($_GET['q'] ?? '');
$status = trim($_GET['status'] ?? '');
$categoryFilter = trim($_GET['category'] ?? '');
$page = max(1, intval($_GET['page'] ?? 1));
$limit = 12;
$offset = ($page - 1) * $limit;

$items = getItems($conn, [
    'q' => $search,
    'status' => $status,
    'category' => $categoryFilter,
    'limit' => $limit,
    'offset' => $offset
]);

$total_items = getItemsCount($conn, [
    'q' => $search,
    'status' => $status,
    'category' => $categoryFilter
]);

include_once __DIR__ . '/includes/header.php';
?>
<nav class="navbar">
    <div class="navbar-inner">
        <a href="dashboard.php" class="nav-link">Dashboard</a>
        <a href="browse.php" class="nav-link">Browse</a>
        <a href="campus-map.php" class="nav-link active">Campus Map</a>
        <a href="saved-items.php" class="nav-link">Saved</a>
        <a href="my-lost-items.php" class="nav-link">My Lost</a>
        <a href="my-found-items.php" class="nav-link">My Found</a>
    </div>
</nav>
<div class="container" style="padding: 24px;">
    <h1>Campus Map</h1>
    <p>Items by location (filtered): <?php echo intval($total_items); ?></p>
    <div class="map-placeholder" style="height: 300px; width: 100%; border: 2px dashed #d1d5db; border-radius: 12px; margin-bottom: 24px; display: flex; align-items: center; justify-content: center;">Interactive map placeholder</div>
    <div class="items-grid" style="display:grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap:16px;">
        <?php if (empty($items)): ?>
            <div>No map items found.</div>
        <?php else: ?>
            <?php foreach ($items as $item): ?>
            <div style="background:#fff; padding: 16px; border-radius: 12px; box-shadow: 0 3px 8px rgba(0,0,0,0.06);">
                <h3><?php echo htmlspecialchars($item['title']); ?></h3>
                <p><strong>Location:</strong> <?php echo htmlspecialchars($item['location']); ?></p>
                <p><strong>Status:</strong> <?php echo htmlspecialchars(ucfirst($item['status'])); ?></p>
                <p><strong>Category:</strong> <?php echo htmlspecialchars($item['category']); ?></p>
            </div>
            <?php endforeach; ?>
        <?php endif; ?>
    </div>
</div>
<?php include_once __DIR__ . '/includes/footer.php'; ?>
