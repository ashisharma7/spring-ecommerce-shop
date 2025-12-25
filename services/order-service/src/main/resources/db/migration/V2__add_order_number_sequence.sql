-- Create sequence for business order numbers
CREATE SEQUENCE IF NOT EXISTS order_number_seq
    START WITH 100000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;