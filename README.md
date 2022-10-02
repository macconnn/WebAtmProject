# webatm


According the code below to create the `SQL database and tables` and follow the specified version below

`Tomcat Version : 7.0.109`
`JDK Version : 1.8.0`

## Step 1
```
create database webatm;
```
## Step 2
```
CREATE TABLE userinfo(
user_id int(4) unsigned NOT NULL AUTO_INCREMENT,
ID char(10) NOT NULL ,
name varchar(20) NOT NULL,
account varchar(30) NOT NULL,
password varchar(100) NOT NULL,
PRIMARY KEY(user_id ),
UNIQUE KEY(ID,account)
)ENGINE=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

## Step 3
```
create table userbalance(
user_id int(4) unsigned NOT NULL AUTO_INCREMENT,
account_balance int unsigned DEFAULT 0 NOT NULL,
debt int unsigned DEFAULT 0 NOT NULL,
PRIMARY KEY(user_id )
)ENGINE=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```
