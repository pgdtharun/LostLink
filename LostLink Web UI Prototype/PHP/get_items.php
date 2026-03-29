<?php
// get_items.php - Fetch items for display with filtering and pagination
require_once __DIR__ . '/includes/db.php';

function getItems($conn, $options = []) {
    $search = trim($options['q'] ?? '');
    $status = trim($options['status'] ?? '');
    $category = trim($options['category'] ?? '');
    $location = trim($options['location'] ?? '');
    $limit = intval($options['limit'] ?? 12);
    $offset = intval($options['offset'] ?? 0);

    $sql = 'SELECT items.*, users.name, users.avatar FROM items JOIN users ON items.user_id = users.id WHERE 1=1';
    $types = '';
    $params = [];

    if ($search !== '') {
        $sql .= ' AND (items.title LIKE ? OR items.description LIKE ?)';
        $types .= 'ss';
        $params[] = "%{$search}%";
        $params[] = "%{$search}%";
    }
    if ($status !== '') {
        $sql .= ' AND items.status = ?';
        $types .= 's';
        $params[] = $status;
    }
    if ($category !== '') {
        $sql .= ' AND items.category = ?';
        $types .= 's';
        $params[] = $category;
    }
    if ($location !== '') {
        $sql .= ' AND items.location LIKE ?';
        $types .= 's';
        $params[] = "%{$location}%";
    }

    $sql .= ' ORDER BY items.created_at DESC LIMIT ? OFFSET ?';
    $types .= 'ii';
    $params[] = $limit;
    $params[] = $offset;

    $stmt = $conn->prepare($sql);
    if (!$stmt) {
        return [];
    }

    // bind params by reference
    $bindNames = [];
    $bindNames[] = &$types;
    foreach ($params as $key => $val) {
        $bindNames[] = &$params[$key];
    }
    call_user_func_array([$stmt, 'bind_param'], $bindNames);

    $stmt->execute();
    $result = $stmt->get_result();
    return $result ? $result->fetch_all(MYSQLI_ASSOC) : [];
}

function getItemsCount($conn, $options = []) {
    $search = trim($options['q'] ?? '');
    $status = trim($options['status'] ?? '');
    $category = trim($options['category'] ?? '');
    $location = trim($options['location'] ?? '');

    $sql = 'SELECT COUNT(*) as total FROM items WHERE 1=1';
    $types = '';
    $params = [];

    if ($search !== '') {
        $sql .= ' AND (title LIKE ? OR description LIKE ?)';
        $types .= 'ss';
        $params[] = "%{$search}%";
        $params[] = "%{$search}%";
    }
    if ($status !== '') {
        $sql .= ' AND status = ?';
        $types .= 's';
        $params[] = $status;
    }
    if ($category !== '') {
        $sql .= ' AND category = ?';
        $types .= 's';
        $params[] = $category;
    }
    if ($location !== '') {
        $sql .= ' AND location LIKE ?';
        $types .= 's';
        $params[] = "%{$location}%";
    }

    $stmt = $conn->prepare($sql);
    if (!$stmt) {
        return 0;
    }

    if ($types !== '') {
        $bindNames = [];
        $bindNames[] = &$types;
        foreach ($params as $key => $val) {
            $bindNames[] = &$params[$key];
        }
        call_user_func_array([$stmt, 'bind_param'], $bindNames);
    }

    $stmt->execute();
    $result = $stmt->get_result();
    $row = $result ? $result->fetch_assoc() : null;
    return $row ? intval($row['total']) : 0;
}

