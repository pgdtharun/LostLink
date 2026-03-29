# LostLink PHP Replica

## Structure
- `php/` — All PHP files
  - `config/` — Configuration files (e.g., config.php)
  - `includes/` — Common includes (db.php, header.php, footer.php, functions.php)
  - `assets/` — Static assets
    - `css/` — Stylesheets
    - `js/` — JavaScript files
    - `images/` — Images
  - `*.php` — Main pages (replicas of HTML)

## Hosting
- Place the `php/` folder inside your XAMPP `htdocs` directory.
- Access via: `http://localhost/LostLink/php/`
- Edit `config/config.php` for database settings.

## Next Steps
- Copy HTML content into each corresponding PHP file between header/footer includes.
- Implement dynamic features as needed.
