CREATE DATABASE order_db;
CREATE DATABASE inventory_db;
CREATE DATABASE catalog_db;
CREATE DATABASE payment_db;

-- dedicated users per service
CREATE USER order_user WITH PASSWORD 'order_pass';
CREATE USER inventory_user WITH PASSWORD 'inventory_pass';
CREATE USER payment_user WITH PASSWORD 'payment_pass';
CREATE USER catalog_user WITH PASSWORD 'catalog_pass';

GRANT ALL PRIVILEGES ON DATABASE order_db TO order_user;
GRANT ALL PRIVILEGES ON DATABASE inventory_db TO inventory_user;
GRANT ALL PRIVILEGES ON DATABASE payment_db TO payment_user;
GRANT ALL PRIVILEGES ON DATABASE catalog_db TO catalog_user;

\connect order_db;
GRANT USAGE, CREATE ON SCHEMA public TO order_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO order_user;

\connect inventory_db;
GRANT USAGE, CREATE ON SCHEMA public TO inventory_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO inventory_user;

\connect payment_db;
GRANT USAGE, CREATE ON SCHEMA public TO payment_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO payment_user;

\connect catalog_db;
GRANT USAGE, CREATE ON SCHEMA public TO catalog_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO catalog_user;