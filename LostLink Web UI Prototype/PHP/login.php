<?php
// login.php - User login handler
require_once __DIR__ . '/includes/db.php';
session_start();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = trim($_POST['email'] ?? '');
    $password = $_POST['password'] ?? '';
    if ($email && $password) {
        $stmt = $conn->prepare('SELECT id, name, password, avatar FROM users WHERE email = ?');
        $stmt->bind_param('s', $email);
        $stmt->execute();
        $stmt->store_result();
        if ($stmt->num_rows === 1) {
            $stmt->bind_result($id, $name, $hash, $avatar);
            $stmt->fetch();
            if (password_verify($password, $hash)) {
                $_SESSION['user_id'] = $id;
                $_SESSION['user_name'] = $name;
                $_SESSION['user_avatar'] = $avatar;
                header('Location: dashboard.php');
                exit();
            } else {
                $error = 'Invalid credentials.';
            }
        } else {
            $error = 'User not found.';
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
        <h2 class="text-center">Sign In to LostLink</h2>
        <?php if (!empty($error)): ?>
            <div class="alert alert-danger"><?php echo htmlspecialchars($error); ?></div>
        <?php endif; ?>
        <form method="post" action="login.php" class="form">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required autofocus class="form-control" placeholder="Enter your email">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required class="form-control" placeholder="Enter your password">
            </div>
            <button type="submit" class="btn btn-primary w-full">Sign In</button>
        </form>
        <div class="text-center" style="margin-top: 16px;">
            <span>Don't have an account?</span>
            <a href="register.php" class="btn btn-link">Create Account</a>
        </div>
    </div>
</div>
<?php include_once __DIR__ . '/includes/footer.php'; ?>
