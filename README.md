Design Of The Project

There are 9 tables: roles, user, roomType, room, guest, booking, housekeepingSchedule, inventory
and amenities.

roles table has 3 attributes:

• role_id is unique for each role.

• role_name shows the name of each role. (administrator, receptionist, housekeeping)

• role_desc basically describes related jobs description.

user table has 5 attributes.

• user_id uniquely identifies each user.

• role_id attribute is a foreign key that references roles table’s role_id attribute.

• user_name attribute stores the name of the user.

• user_phone attribute stores the phone number of the user.

• user_mail attribute stores the e-mail of the user.

roomType table has 3 attributes.

• rType_id uniquely identifies each roomType.

• r_desc describes the room type.

• r_capacity stores the number of people that can stay in related room type.

room table has 3 attributes:

• room_id uniquely identifies each room.

• room_name describes the name of the room.

• room_type is a foreign key that references roomType table’s rType_id.

guest table has 3 attributes.

• guest_id uniquely identifies each guest.

• guest_name stores the name of the guest.

• guest_phone stores the phone number of the guest.

booking table has 11 attributes.


• book_id uniquely identifies each booking.

• room_name references room table.

• user_id references user table.

• guest_id references guest table.

• guest_count stores the number of guests that a booking has.

• checkin_date stores the checkin date of the guest.

• checkout_date stores the checkout date of the guest.

• is_paid attribute is 1 if payment is done, otherwise it is 0.


• Price attribute stores the total price of the booking.

• Is_booked

CS 202 Project Report

• confirmation_status attribute is set to :

0: denied

1: pending

2:confirmed

3:checked-in

4:checked-out

housekeepingSchedule table has 5 attributes.

• Schedule_id uniquely identifies each housekeepingSchedule.


• user_id shows the user that is responsible for cleaning.

• room_id is the room that is cleaned.

• is_cleaned attribute is 0 if the room is dirty, otherwise it is 1.

• cleaning_date stores the date of the cleaning.

Inventory table has 3 attributes.

• room_id stores room id.

• amenities_id stores amenity id.

• Quantity stores how many specific amenity is stored in a specific room.

amenities table has 3 attributes.

amenities_id uniquely identifies each amenity.

amenities_name describe the amenity.


Design Choices:
System takes checkin and checkout dates from the guest, if there is a room between given dates
guest can book a room.

When guests checkout from a room, iscleaned attribute in housekeepingSchedule is set to 0. If it’s
not 1, new guests can not checkin to that room.

If a guest is deleted from guest table, same guest’s bookings that are in the past are also deleted.

If user is deleted from user table or room_name is deleted from room table, corresponding user_id
attribute in booking table is set to null but in housekeepingSchedule corresponding row is deleted
from database.

If role_id is deleted from roles, role_id in user table is set to null.

If rType_id is deleted, room_type in room table is set to null.

There is a view that ensures housekeeping can only view room availability but nothing about Guest
information.

DML file that is provided should be runned before DDL is runned.
