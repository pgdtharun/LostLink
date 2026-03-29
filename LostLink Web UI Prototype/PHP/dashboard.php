<?php
include_once __DIR__ . '/config/config.php';
include_once __DIR__ . '/includes/auth.php';
include_once __DIR__ . '/includes/header.php';
require_once __DIR__ . '/get_items.php';
require_once __DIR__ . '/includes/db.php';

$search = trim($_GET['q'] ?? '');
$status = trim($_GET['status'] ?? '');
$categoryFilter = trim($_GET['category'] ?? '');
$location = trim($_GET['location'] ?? '');
$page = max(1, intval($_GET['page'] ?? 1));
$limit = 12;
$offset = ($page - 1) * $limit;

$items = getItems($conn, [
	'q' => $search,
	'status' => $status,
	'category' => $categoryFilter,
	'location' => $location,
	'limit' => $limit,
	'offset' => $offset,
]);

$total_items = getItemsCount($conn, [
	'q' => $search,
	'status' => $status,
	'category' => $categoryFilter,
	'location' => $location,
]);

$items_this_month = 0;
$users = 0;
$recovery_rate = 0;

if ($stmt = $conn->prepare('SELECT COUNT(*) AS total FROM items WHERE created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY)')) {
	$stmt->execute();
	$stmt->bind_result($items_this_month);
	$stmt->fetch();
	$stmt->close();
}

if ($stmt = $conn->prepare('SELECT COUNT(*) AS total FROM users')) {
	$stmt->execute();
	$stmt->bind_result($users);
	$stmt->fetch();
	$stmt->close();
}

if ($stmt = $conn->prepare('SELECT COUNT(*) AS found_count FROM items WHERE status = "found"')) {
	$stmt->execute();
	$stmt->bind_result($found_count);
	$stmt->fetch();
	$stmt->close();
}

if ($items_this_month > 0) {
	$recovery_rate = ($found_count / $items_this_month) * 100;
}

?>
<nav class="navbar">
	<div class="navbar-inner">
		<a href="index.php" class="logo">
			<!-- ...logo SVG... -->
			<span class="logo-text">LostLink</span>
		</a>
		<div class="search-bar">
			<form method="get" action="dashboard.php" style="display:flex; gap:8px; align-items: center;">
				<input name="q" type="text" value="<?php echo htmlspecialchars($search); ?>" placeholder="Search items..." class="form-input" style="width: 240px;" />
				<select name="status" class="form-input" style="width: 130px;">
					<option value="">Any status</option>
					<option value="lost" <?php echo ($status==='lost') ? 'selected' : ''; ?>>Lost</option>
					<option value="found" <?php echo ($status==='found') ? 'selected' : ''; ?>>Found</option>
					<option value="claimed" <?php echo ($status==='claimed') ? 'selected' : ''; ?>>Claimed</option>
					<option value="pending" <?php echo ($status==='pending') ? 'selected' : ''; ?>>Pending</option>
				</select>
				<select name="category" class="form-input" style="width: 150px;">
					<option value="">All categories</option>
					<option value="Electronics" <?php echo ($categoryFilter==='Electronics') ? 'selected' : ''; ?>>Electronics</option>
					<option value="Bags & Wallets" <?php echo ($categoryFilter==='Bags & Wallets') ? 'selected' : ''; ?>>Bags & Wallets</option>
					<option value="ID & Cards" <?php echo ($categoryFilter==='ID & Cards') ? 'selected' : ''; ?>>ID & Cards</option>
					<option value="Books & Notes" <?php echo ($categoryFilter==='Books & Notes') ? 'selected' : ''; ?>>Books & Notes</option>
					<option value="Other" <?php echo ($categoryFilter==='Other') ? 'selected' : ''; ?>>Other</option>
				</select>
				<button type="submit" class="btn btn-primary btn-sm btn-rounded">Go</button>
			</form>
		</div>
		<div class="navbar-actions">
			<button class="notification-btn" title="Notifications">
				<!-- ...notification icon... -->
				<span class="notification-badge"></span>
			</button>
			<a href="report.php" class="btn btn-primary btn-sm btn-rounded">Report</a>
			<a href="profile.php" class="nav-avatar" title="Profile">AP</a>
		</div>
	</div>
</nav>
<div class="app-layout">
	<aside class="sidebar">
		<!-- ...sidebar links, update all .html to .php... -->
		<div class="sidebar-section">
			<div class="sidebar-section-title">Menu</div>
			<a href="dashboard.php" class="sidebar-link active">Dashboard</a>
			<a href="browse.php" class="sidebar-link">Browse All</a>
			<a href="campus-map.php" class="sidebar-link">Campus Map</a>
			<a href="saved-items.php" class="sidebar-link">Saved Items<span class="badge-count">3</span></a>
		</div>
		<div class="sidebar-section">
			<div class="sidebar-section-title">My Reports</div>
			<a href="my-lost-items.php" class="sidebar-link">My Lost Items<span class="badge-count">2</span></a>
			<a href="my-found-items.php" class="sidebar-link">My Found Items<span class="badge-count">1</span></a>
			<a href="messages.php" class="sidebar-link">Messages<span class="badge-count">5</span></a>
		</div>
		<div class="sidebar-section">
			<div class="sidebar-section-title">Categories</div>
			<a href="category-electronics.php" class="sidebar-link">Electronics</a>
			<a href="category-bags.php" class="sidebar-link">Bags & Wallets</a>
			<a href="category-id-cards.php" class="sidebar-link">ID & Cards</a>
			<a href="category-books.php" class="sidebar-link">Books & Notes</a>
			<a href="category-other.php" class="sidebar-link">Other</a>
		</div>
	</aside>
	<main class="main-content">
		<!-- ...stats, page header, filters, items grid, and pagination (as in HTML, update links to .php)... -->
		<div class="stats-grid" style="display: flex; gap: 24px; margin-bottom: 32px;">
			<div class="stat-card" style="background: #fff; border-radius: 12px; padding: 24px 32px; box-shadow: 0 2px 8px rgba(0,0,0,0.04);">
				<div class="stat-label">Items This Month</div>
			<div class="stat-value" style="font-size: 2rem; font-weight: bold;"><?php echo intval($items_this_month); ?></div>
		</div>
		<div class="stat-card" style="background: #fff; border-radius: 12px; padding: 24px 32px; box-shadow: 0 2px 8px rgba(0,0,0,0.04);">
			<div class="stat-label">Active Users</div>
			<div class="stat-value" style="font-size: 2rem; font-weight: bold;"><?php echo intval($users); ?></div>
		</div>
		<div class="stat-card" style="background: #fff; border-radius: 12px; padding: 24px 32px; box-shadow: 0 2px 8px rgba(0,0,0,0.04);">
			<div class="stat-label">Recovery Rate</div>
			<div class="stat-value" style="font-size: 2rem; font-weight: bold;"><?php echo number_format(floatval($recovery_rate), 1); ?>%</div>
			</div>
		</div>
		<div class="page-header flex justify-between items-center">
			<div>
				<h1 class="page-title">Browse Items</h1>
				<p class="page-subtitle"><?php echo intval($total_items); ?> items found</p>
			</div>
			<div class="flex gap-sm">
				<button class="btn btn-ghost btn-sm" title="Grid view"></button>
				<button class="btn btn-ghost btn-sm" title="List view"></button>
			</div>
		</div>
		<div class="filters-bar">
			<!-- ...filter chips... -->
		</div>
		<div class="items-grid stagger-children" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 24px;">
			<?php if (empty($items)): ?>
				<div style="grid-column: 1 / -1; text-align: center; padding: 40px;">No items found. Try a different search or filter.</div>
			<?php else: ?>
				<?php foreach ($items as $item): ?>
					<a href="item-detail.php?id=<?php echo intval($item['id']); ?>" class="item-card animate-fade-in-up" style="background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); padding: 20px 24px; display: flex; flex-direction: column; gap: 8px; text-decoration: none; color: inherit;">
						<div style="display: flex; align-items: center; gap: 12px;">
						<?php $icon = isset($item['icon']) && $item['icon'] ? $item['icon'] : 'bag'; ?>
						<img src="assets/icons/<?php echo htmlspecialchars($icon); ?>.svg" alt="<?php echo htmlspecialchars($item['category'] ?? ''); ?> icon" style="width: 32px; height: 32px;">
							<span style="font-weight: 600; font-size: 1.1rem;"> <?php echo htmlspecialchars($item['title']); ?> </span>
						</div>
						<div style="color: #888; font-size: 0.95rem;">Category: <?php echo htmlspecialchars($item['category']); ?></div>
						<div style="color: #888; font-size: 0.95rem;">Location: <?php echo htmlspecialchars($item['location']); ?></div>
						<div style="color: #888; font-size: 0.95rem;">Date: <?php echo htmlspecialchars($item['created_at']); ?></div>
						<div style="margin-top: 8px;">
							<span class="badge" style="background: <?php echo htmlspecialchars(strtolower($item['status']))==='lost' ? '#fee2e2' : '#d1fae5'; ?>; color: <?php echo htmlspecialchars(strtolower($item['status']))==='lost' ? '#b91c1c' : '#065f46'; ?>; padding: 2px 10px; border-radius: 8px; font-size: 0.85rem; font-weight: 500;">
								<?php echo htmlspecialchars(ucfirst($item['status'])); ?>
							</span>
						</div>
					</a>
				<?php endforeach; ?>
			<?php endif; ?>
		</div>
		<div class="flex justify-center items-center gap-sm" style="margin-top: 40px;">
			<?php
			$total_pages = max(1, ceil($total_items / $limit));
			$current_uri = 'dashboard.php';
			$base_params = [];
			if ($search !== '') { $base_params['q'] = $search; }
			if ($status !== '') { $base_params['status'] = $status; }
			if ($categoryFilter !== '') { $base_params['category'] = $categoryFilter; }
			if ($location !== '') { $base_params['location'] = $location; }
			for ($p = 1; $p <= $total_pages && $p <= 10; $p++) {
				$params = $base_params;
				$params['page'] = $p;
				$link = $current_uri . '?' . http_build_query($params);
				$active = ($p === $page) ? ' background: #059669; color: #fff;' : '';
				echo "<a href=\"{$link}\" class=\"btn btn-ghost btn-sm\" style=\"{$active}\">{$p}</a>";
			}
			?>
		</div>
	</main>
</div>
<?php include_once __DIR__ . '/includes/footer.php'; ?>
