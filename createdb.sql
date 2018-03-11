
CREATE TABLE YelpUser
(
	UserName VARCHAR2(255),
    Yelping_Since VARCHAR2(30),
    CoolVotes NUMBER,
	UsefulVotes NUMBER,	
    ReviewCount NUMBER,
    UserId VARCHAR2(60),
    Fans NUMBER,
    AverageStars FLOAT,
    UserType VARCHAR2(60),
	FunnyVotes NUMBER,
    PRIMARY KEY (UserId)
);

CREATE TABLE Friends
(
    UserId VARCHAR2(40),
    PRIMARY KEY (UserId,FriendId),
	FriendId VARCHAR2(50),
    FOREIGN  KEY (UserId) REFERENCES YelpUser (UserId) ON DELETE CASCADE
);


CREATE TABLE Business
(
    BusinessId VARCHAR2(50),
	Longitude FLOAT,
    FullAddress VARCHAR2(255),
    IsOpen VARCHAR2(20),
    ReviewCount NUMBER,
    BName VARCHAR2(255),
    State VARCHAR2(255),
    Stars FLOAT,
	City VARCHAR2(255),
    Latitude FLOAT,
    BType VARCHAR2(60),
    PRIMARY KEY (BusinessId)
);

CREATE TABLE BusinessCategory
(
    CategoryName VARCHAR2(255),
    BusinessId VARCHAR2(70),
    PRIMARY KEY (BusinessId, CategoryName),
    FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE
);

CREATE TABLE BusinessSubCategory
(   BusinessId VARCHAR2(100),
    SubCategoryName VARCHAR2(255),
    PRIMARY KEY (BusinessId,SubCategoryName),
    FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE   
);
SELECT count(*) FROM CHECKIN;
CREATE TABLE CheckIN(
CheckinCount NUMBER,
BusinessId VARCHAR2(100),
CheckinHour NUMBER,
CheckinDay VARCHAR2(100),
PRIMARY KEY (BusinessId, CheckinHour,CheckinDay,CheckinCount),
FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE
);

CREATE TABLE Review
(
    FunnyVote number,
    UsefulVote number,
    CoolVote number,
    UserId varchar2(50),    
    Stars number,
    RDate varchar2(20),
    RType varchar2(255),
	RText clob,
    BusinessId varchar2(50),
	ReviewId varchar2(50),
    primary key (ReviewId),
    FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE,
    FOREIGN  KEY (UserId) REFERENCES YelpUser (UserId) ON DELETE CASCADE
);

