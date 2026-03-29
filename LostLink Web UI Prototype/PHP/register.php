<?php
// register.php - User registration handler
require_once __DIR__ . '/includes/db.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = trim($_POST['name'] ?? '');
    $email = trim($_POST['email'] ?? '');
    $password = $_POST['password'] ?? '';
    $avatar = strtoupper(substr($name, 0, 2));

    if ($name && $email && $password) {
        $hash = password_hash($password, PASSWORD_DEFAULT);
        $stmt = $conn->prepare('INSERT INTO users (name, email, password, avatar) VALUES (?, ?, ?, ?)');
        $stmt->bind_param('ssss', $name, $email, $hash, $avatar);
        if ($stmt->execute()) {
            header('Location: login.php?registered=1');
            exit();
        } else {
            $error = 'Registration failed: ' . $conn->error;
        }
    } else {
        $error = 'All fields are required.';
    }
}
?>
<?php include_once __DIR__ . '/includes/header.php'; ?>
<style>
    body {
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #f6fff7;
    }
    .auth-container {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100vw;
        height: 100vh;
    }
    .auth-card {
        background: #fff;
        border-radius: 16px;
        box-shadow: 0 8px 32px rgba(0,0,0,0.08);
        padding: 32px 32px 24px 32px;
        min-width: 320px;
        max-width: 360px;
        margin: 0 auto;
    }
</style>
<div class="auth-container">
        <div class="auth-card">
                <h2 class="text-center">Create a New Account</h2>
                <?php if (!empty($error)): ?>
                        <div class="alert alert-danger"><?php echo htmlspecialchars($error); ?></div>
                <?php endif; ?>
                <form method="post" action="register.php" class="form">
                        <div class="form-group">
                                <label for="name">Full Name</label>
                                <input type="text" id="name" name="name" required autofocus class="form-control" placeholder="Enter your name">
                        </div>
                        <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email" id="email" name="email" required class="form-control" placeholder="Enter your email">
                        </div>
                        <div class="form-group">
                                <label for="password">Password</label>
                                <input type="password" id="password" name="password" required class="form-control" placeholder="Create a password">
                        </div>
                        <button type="submit" class="btn btn-primary w-full">Create Account</button>
                </form>
                <div class="text-center" style="margin-top: 16px;">
                        <span>Already have an account?</span>
                        <a href="login.php" class="btn btn-link">Sign In</a>
                </div>
        </div>
</div>
<?php include_once __DIR__ . '/includes/footer.php'; ?>
