<?php
// functions.php - Common functions for LostLink PHP replica

function redirect($url) {
    header('Location: ' . $url);
    exit();
}
// Add more utility functions as needed
