<?php
include_once __DIR__ . '/config/config.php';
include_once __DIR__ . '/includes/header.php';
?>
<nav class="navbar">
	<div class="navbar-inner">
		<a href="index.php" class="logo">
			<!-- ...logo SVG... -->
			<span class="logo-text">LostLink</span>
		</a>
		<div class="navbar-links">
			<a href="index.php" class="nav-link active">Home</a>
			<a href="dashboard.php" class="nav-link">Browse Items</a>
			<a href="report.php" class="nav-link">Report</a>
			<a href="#about" class="nav-link">About</a>
		</div>
		<div class="navbar-actions">
			<a href="login.php" class="btn btn-ghost btn-sm">Log In</a>
			<a href="register.php" class="btn btn-primary btn-sm btn-rounded">Get Started</a>
		</div>
	</div>
</nav>
<section class="hero">
	<div class="hero-content">
		<div class="hero-left animate-fade-in-up">
			<!-- ...hero badge, title, subtitle, actions, stats... -->
			<div class="hero-badge">University of NSBM</div>
			<h1 class="hero-title">Reuniting Campus<br/><span>With Lost Belongings</span></h1>
			<p class="hero-subtitle">LostLink is your university's trusted platform to report, search, and recover lost items across campus. Quick, simple, and community-driven.</p>
			<div class="hero-actions">
				<a href="dashboard.php" class="btn btn-primary btn-lg btn-rounded">Browse Lost Items</a>
				<a href="report.php" class="btn btn-secondary btn-lg btn-rounded">Report an Item</a>
			</div>
			<div class="hero-stats">
				<div class="hero-stat"><div class="hero-stat-number">1,240+</div><div class="hero-stat-label">Items Recovered</div></div>
				<div class="hero-stat"><div class="hero-stat-number">3,800+</div><div class="hero-stat-label">Active Users</div></div>
				<div class="hero-stat"><div class="hero-stat-number">92%</div><div class="hero-stat-label">Recovery Rate</div></div>
			</div>
		</div>
		<div class="hero-right animate-fade-in-up" style="animation-delay: 200ms;">
			<!-- ...auth card... -->
		</div>
	</div>
</section>
<section style="padding: 96px 24px; background: var(--white);" id="about">
	<div class="container">
		<!-- ...features section... -->
	</div>
</section>
<section style="padding: 96px 24px;">
	<div class="container">
		<!-- ...recent items section, update dashboard.html to dashboard.php... -->
		<a href="dashboard.php" class="btn btn-secondary btn-rounded">View All</a>
		<div class="items-grid stagger-children">
			<!-- ...item cards... -->
		</div>
	</div>
</section>
<footer class="footer">
	<div class="footer-inner">
		<div class="footer-col">
			<a href="index.php" class="logo" style="margin-bottom: 16px;">
				<!-- ...logo SVG... -->
				<span class="logo-text">LostLink</span>
			</a>
			<p style="font-size: 0.875rem; color: var(--gray-500); max-width: 260px; line-height: 1.7;">Helping the University of Plymouth community recover lost belongings since 2025.</p>
		</div>
		<div class="footer-col">
			<h4>Platform</h4>
			<a href="dashboard.php">Browse Items</a>
			<a href="report.php">Report Lost</a>
			<a href="report.php">Report Found</a>
			<a href="#">How It Works</a>
		</div>
		<div class="footer-col">
			<h4>Support</h4>
			<a href="#">FAQ</a>
			<a href="#">Contact Us</a>
			<a href="#">Privacy Policy</a>
			<a href="#">Terms of Use</a>
		</div>
		<div class="footer-col">
			<h4>University</h4>
			<a href="#">Plymouth Portal</a>
			<a href="#">Campus Map</a>
			<a href="#">Student Support</a>
			<a href="#">Security Office</a>
		</div>
	</div>
	<div class="footer-bottom">&copy; 2025 LostLink — Group 84, University of Plymouth. All rights reserved.</div>
</footer>
<?php include_once __DIR__ . '/includes/footer.php'; ?>
