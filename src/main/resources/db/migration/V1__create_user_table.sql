-- Create the user table
CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    user_password VARCHAR(255), -- Assuming this is where you store the password
    enum_role VARCHAR(255), -- Assuming this is where you store the user role
    profile_image BLOB, -- Assuming this is where you store the profile image as binary data
    expertise VARCHAR(255),
    location VARCHAR(255),
    bio TEXT, -- Assuming this is where you store longer text like a biography
    industry VARCHAR(255),
    mentoring_required_for VARCHAR(255),
    charge_per_hour DOUBLE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    last_updated TIMESTAMP NOT NULL,
    last_logout TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_email UNIQUE (email)
);
