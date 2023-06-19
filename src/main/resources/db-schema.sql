create table if not exists Sample(
    id serial PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    data text,
    value int default 0
);

CREATE TABLE if not exists  roles(
    role_id INT PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    role_desc TEXT
    );


CREATE TABLE if not exists  user(
	user_id INT PRIMARY KEY AUTO_INCREMENT,
    role_id INT,
    user_name VARCHAR(100) NOT NULL,
    user_phone VARCHAR(15) NOT NULL,
    user_mail VARCHAR(255) NOT NULL,
    FOREIGN KEY(role_id) REFERENCES roles(role_id) ON DELETE SET NULL
    );

CREATE TABLE if not exists  roomType(
	rType_id INT PRIMARY KEY,
    r_capacity INT NOT NULL,
    r_desc TEXT
);

CREATE TABLE if not exists  amenities(
	amenities_id INT PRIMARY KEY AUTO_INCREMENT,
    amenities_name VARCHAR(100));


CREATE TABLE if not exists  room(
    room_id INT PRIMARY KEY AUTO_INCREMENT,
	room_name VARCHAR(50),
    room_type INT,
    FOREIGN KEY (room_type) REFERENCES roomType(rType_id) ON DELETE SET NULL);

CREATE TABLE if not exists  inventory(
	room_id INT,
    amenities_id INT,
    quantity INT,
	FOREIGN KEY (amenities_id) REFERENCES amenities(amenities_id) ON DELETE SET NULL,
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE SET NULL
);

CREATE TABLE if not exists  guest(
	guest_id INT PRIMARY KEY AUTO_INCREMENT,
    guest_name VARCHAR(100) NOT NULL,
    guest_phone VARCHAR(12) NOT NULL
);

CREATE TABLE if not exists  booking(
	book_id INT PRIMARY KEY AUTO_INCREMENT,
    room_id INT,
    user_id INT,
    guest_id INT,
    guest_count INT,
    is_paid INT default 0,
    price DOUBLE,
    confirmation_status INT default 1,
	is_booked INT default 0,
    checkin_date DATE NOT NULL,
    checkout_date DATE NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL,
    FOREIGN KEY (guest_id) REFERENCES guest(guest_id)ON DELETE CASCADE
    );

CREATE TABLE if not exists  housekeepingSchedule(
	schedule_id INT PRIMARY KEY AUTO_INCREMENT,
	user_id INT,
    room_id INT,
	is_cleaned INT NOT NULL,
    cleaning_date DATE,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES room(room_id)ON DELETE CASCADE);

INSERT INTO roles values (1, 'administrator', "oversee all aspects of hotel operations, including human resources, guest services, facilities maintenance and finance and accounting.");
INSERT INTO roles values (2, 'receptionist', "greeting guests as they come in, managing the check-in and check-out process, answering questions and requests, and helping with administrative tasks at the front desk.");
INSERT INTO roles values (3, 'housekeeping', "Performs cleaning duties in all guest areas and back of house.");

INSERT INTO user(role_id, user_name, user_phone, user_mail) values (1,'Eren', '05346680334', 'eren.gulhan@ozu.edu.tr');
INSERT INTO user(role_id, user_name, user_phone, user_mail) values (1,'Kıvanç', '05326224152', 'kivanc.serefoglu@ozu.edu.tr');
INSERT INTO user(role_id, user_name, user_phone, user_mail) values (2,'Emirhan', '053476989212', 'emirhan.cavusoglu@ozu.edu.tr');
INSERT INTO user(role_id, user_name, user_phone, user_mail) values (2,'Ahmet', '05348291821', 'ahmet.haskoylu@ozu.edu.tr');
INSERT INTO user(role_id, user_name, user_phone, user_mail) values (3,'Tuğberk', '05348292312', 'tugberk.cil@ozu.edu.tr');
INSERT INTO user(role_id, user_name, user_phone, user_mail) values (3,'Hamza', '05348215221', 'hamza.haskoylu@ozu.edu.tr');
INSERT INTO user(role_id, user_name, user_phone, user_mail) values (3,'Hüseyin', '05347825162', 'huseyin.eryılmaz@ozu.edu.tr');

INSERT INTO roomType values (1, 1, 'Single Rooms');
INSERT INTO roomType values (2, 2, 'Double Room with separate bed');
INSERT INTO roomType values (3, 2, 'Double Room with one bed');
INSERT INTO roomType values (4, 2, 'Deluxe Rooms with one bed');
INSERT INTO roomType values (5, 4, 'Deluxe Rooms with two bed');
INSERT INTO roomType values (6, 2, 'King Suite with one bed');
INSERT INTO roomType values (7, 10, 'King Suite with five bed');
INSERT INTO roomType values (8, 5, 'King Suite with three bed');

INSERT INTO amenities(amenities_name) values('coffe maker');
INSERT INTO amenities(amenities_name) values('air conditioning');
INSERT INTO amenities(amenities_name) values('fridge');
INSERT INTO amenities(amenities_name) values('hair dryer');



INSERT INTO room(room_name,room_type) values ("A",1);
INSERT INTO room(room_name,room_type) values ("B",2);
INSERT INTO room(room_name,room_type) values ("B",3);
INSERT INTO room(room_name,room_type) values ("B",4);
INSERT INTO room(room_name,room_type) values ("B",5);
INSERT INTO room(room_name,room_type) values ("B",6);
INSERT INTO room(room_name,room_type) values ("B",7);
INSERT INTO room(room_name,room_type) values ("A",8);
INSERT INTO room(room_name,room_type) values ("E",8);
INSERT INTO room(room_name,room_type) values ("D",7);
INSERT INTO room(room_name,room_type) values ("C",6);
INSERT INTO room(room_name,room_type) values ("A",5);
INSERT INTO room(room_name,room_type) values ("B",4);


INSERT INTO inventory values(1, 1, 1);
INSERT INTO inventory values(2, 1, 1);
INSERT INTO inventory values(2, 2, 1);
INSERT INTO inventory values(3, 1, 1);
INSERT INTO inventory values(3, 2, 1);
INSERT INTO inventory values(3, 3, 1);
INSERT INTO inventory values(4, 1, 1);
INSERT INTO inventory values(4, 2, 1);
INSERT INTO inventory values(4, 3, 1);
INSERT INTO inventory values(4, 4, 1);
INSERT INTO inventory values(5, 1, 1);
INSERT INTO inventory values(5, 2, 1);
INSERT INTO inventory values(5, 3, 1);
INSERT INTO inventory values(5, 4, 1);
INSERT INTO inventory values(6, 1, 1);
INSERT INTO inventory values(6, 2, 1);
INSERT INTO inventory values(6, 3, 1);


CREATE VIEW housekeepingscop AS
SELECT room.room_id, booking.is_booked
FROM room
LEFT JOIN booking ON room.room_id = booking.room_id;