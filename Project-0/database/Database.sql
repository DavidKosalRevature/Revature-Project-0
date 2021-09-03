use revature;


create table user(
id int not null auto_increment,
accountType varchar(50),
firstName varchar(50),
lastName varchar(50),
email varchar(100),
username varchar(50),
password varchar(50),
primary key (id)
);

create table BankAccount(
accountId int auto_increment,
customerId int,
accountType varchar(50),
balance double,
primary key (accountId, userId),
foreign key (userId) references user(id)
);

create table transfer(
transferId int auto_increment,
sendId int,
receiveId int,
amount double,
primary key (transferId)
);

create table request(
requestId int auto_increment,
accountType varchar(50),
customerId int,
amount double,
primary key (requestId)
);

Delimiter //

Create Procedure getAllUsers()
begin
	select * from user where accountType = "customer";
end //

Delimiter ;

Delimiter //

Create Procedure getAllRequests()
begin
	select * from request;
end //

Delimiter ;