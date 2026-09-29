
CREATE DATABASE MelodyBeat3;


USE MelodyBeat3;



CREATE TABLE dbo.USER_ACCOUNT (
    UserID       INT IDENTITY(1,1) PRIMARY KEY,
    Username     VARCHAR(60) NOT NULL,
    PasswordHash CHAR(64) NOT NULL,
    FullName     VARCHAR(255) NOT NULL,
    Role         VARCHAR(30) NOT NULL,
    Active       BIT NOT NULL
        CONSTRAINT DF_USER_ACCOUNT_Active DEFAULT (1),
    CreatedAt    DATETIME2 NOT NULL
        CONSTRAINT DF_USER_ACCOUNT_CreatedAt DEFAULT (SYSDATETIME()),

    CONSTRAINT UQ_USER_ACCOUNT_Username UNIQUE (Username),
    CONSTRAINT CK_USER_ACCOUNT_Role CHECK (
        Role IN ('SALES_ASSISTANT', 'STORE_MANAGER') ));


CREATE TABLE dbo.GENRE (
    GenreID   INT IDENTITY(1,1) PRIMARY KEY,
    GenreName VARCHAR(40) NOT NULL,

    CONSTRAINT UQ_GENRE_Name UNIQUE (GenreName));


CREATE TABLE dbo.MUSIC_ITEM (
    ItemID     INT IDENTITY(1,1) PRIMARY KEY,
    Title      VARCHAR(150) NOT NULL,
    Artist     VARCHAR(120) NOT NULL,
    GenreID    INT NOT NULL,
    ItemFormat VARCHAR(30) NOT NULL,
    Price      DECIMAL(10,2) NOT NULL,
    CreatedAt  DATETIME2 NOT NULL
        CONSTRAINT DF_MUSIC_ITEM_CreatedAt DEFAULT (SYSDATETIME()),

    CONSTRAINT FK_MUSIC_ITEM_GENRE
        FOREIGN KEY (GenreID) REFERENCES dbo.GENRE(GenreID),

    CONSTRAINT CK_MUSIC_ITEM_Format CHECK (
        ItemFormat IN ('PHYSICAL_ALBUM', 'DIGITAL_TRACK')),

    CONSTRAINT CK_MUSIC_ITEM_Price CHECK (Price > 0));


CREATE TABLE dbo.PROMOTION (
    PromotionID        INT IDENTITY(1,1) PRIMARY KEY,
    ItemID             INT NOT NULL,
    PromotionName      VARCHAR(150) NOT NULL,
    DiscountPercentage DECIMAL(5,2) NOT NULL,
    StartDate          DATE NOT NULL,
    EndDate            DATE NOT NULL,
    Active             BIT NOT NULL
        CONSTRAINT DF_PROMOTION_Active DEFAULT (1),

    CONSTRAINT FK_PROMOTION_MUSIC_ITEM
        FOREIGN KEY (ItemID) REFERENCES dbo.MUSIC_ITEM(ItemID),

    CONSTRAINT CK_PROMOTION_Discount CHECK (
        DiscountPercentage BETWEEN 0 AND 100),

    CONSTRAINT CK_PROMOTION_Dates CHECK (EndDate >= StartDate));

CREATE TABLE dbo.SALE (
    SaleID         INT IDENTITY(1,1) PRIMARY KEY,
    SaleDateTime   DATETIME2 NOT NULL
        CONSTRAINT DF_SALE_SaleDateTime DEFAULT (SYSDATETIME()),
    UserID         INT NOT NULL,
    Subtotal       DECIMAL(12,2) NOT NULL,
    DiscountAmount DECIMAL(12,2) NOT NULL,
    TotalAmount    DECIMAL(12,2) NOT NULL,

    CONSTRAINT FK_SALE_USER_ACCOUNT
        FOREIGN KEY (UserID) REFERENCES dbo.USER_ACCOUNT(UserID),

    CONSTRAINT CK_SALE_Amounts CHECK (
        Subtotal >= 0
        AND DiscountAmount >= 0
        AND DiscountAmount <= Subtotal
        AND TotalAmount = Subtotal - DiscountAmount));

CREATE TABLE dbo.SALE_ITEM (
    SaleItemID         INT IDENTITY(1,1) PRIMARY KEY,
    SaleID             INT NOT NULL,
    ItemID             INT NOT NULL,
    Quantity           INT NOT NULL,
    UnitPrice          DECIMAL(10,2) NOT NULL,
    DiscountPercentage DECIMAL(5,2) NOT NULL,
    LineTotal          DECIMAL(12,2) NOT NULL,

    CONSTRAINT FK_SALE_ITEM_SALE
        FOREIGN KEY (SaleID) REFERENCES dbo.SALE(SaleID),

    CONSTRAINT FK_SALE_ITEM_MUSIC_ITEM
        FOREIGN KEY (ItemID) REFERENCES dbo.MUSIC_ITEM(ItemID),

    CONSTRAINT CK_SALE_ITEM_Quantity CHECK (Quantity > 0),

    CONSTRAINT CK_SALE_ITEM_Values CHECK (
        UnitPrice > 0
        AND DiscountPercentage BETWEEN 0 AND 100
        AND LineTotal >= 0));


CREATE TABLE dbo.RECEIPT (
    ReceiptID     INT IDENTITY(1,1) PRIMARY KEY,
    SaleID        INT NOT NULL,
    ReceiptNumber VARCHAR(30) NOT NULL,
    IssuedAt      DATETIME2 NOT NULL
        CONSTRAINT DF_RECEIPT_IssuedAt DEFAULT (SYSDATETIME()),

    CONSTRAINT UQ_RECEIPT_SaleID UNIQUE (SaleID),
    CONSTRAINT UQ_RECEIPT_Number UNIQUE (ReceiptNumber),

    CONSTRAINT FK_RECEIPT_SALE
        FOREIGN KEY (SaleID) REFERENCES dbo.SALE(SaleID));


CREATE INDEX IX_MUSIC_ITEM_Genre
    ON dbo.MUSIC_ITEM(GenreID);

CREATE INDEX IX_SALE_SaleDateTime
    ON dbo.SALE(SaleDateTime);

CREATE INDEX IX_PROMOTION_ItemDates
    ON dbo.PROMOTION(ItemID, StartDate, EndDate);

CREATE INDEX IX_SALE_UserID
    ON dbo.SALE(UserID);

CREATE INDEX IX_SALE_ITEM_SaleID
    ON dbo.SALE_ITEM(SaleID);

CREATE INDEX IX_SALE_ITEM_ItemID
    ON dbo.SALE_ITEM(ItemID);
GO

/*
    Test users and passwords:
    manager / Manager@123
    sales1  / User@123

    PasswordHash values are SHA-256 hashes created by PasswordUtil.hash().
*/
INSERT INTO dbo.USER_ACCOUNT
    (Username, PasswordHash, FullName, Role, Active) VALUES
    ('manager','e8392925a98c9c22795d1fc5d0dfee5b9a6943f6b768ec5a2a0c077e5ed119cf','Nimali Perera','STORE_MANAGER',1),
    ('sales1','3e7c19576488862816f13b512cacf3e4ba97dd97243ea0bd6a2ad1642d86ba72','Kamal Silva','SALES_ASSISTANT',1);


INSERT INTO dbo.GENRE (GenreName)
VALUES
    ('POP'),
    ('ROCK'),
    ('JAZZ'),
    ('CLASSICAL'),
    ('HIP_HOP'),
    ('CHILDRENS_MUSIC');
GO

INSERT INTO dbo.MUSIC_ITEM
    (Title, Artist, GenreID, ItemFormat, Price)
VALUES
    ('Midnight Dreams', 'Ocean Echo', 1, 'PHYSICAL_ALBUM', 4500.00),
    ('Electric Storm', 'The Thunder Band', 2, 'PHYSICAL_ALBUM', 5200.00),
    ('Smooth Evening', 'Blue Notes', 3, 'DIGITAL_TRACK', 1500.00),
    ('Moonlight Sonata', 'Ludwig van Beethoven', 4, 'DIGITAL_TRACK', 1200.00),
    ('City Rhymes', 'Urban Voice', 5, 'DIGITAL_TRACK', 1800.00),
    ('Happy Little Stars', 'Kids Music World', 6, 'PHYSICAL_ALBUM', 3000.00);
GO

INSERT INTO dbo.PROMOTION
    (ItemID, PromotionName, DiscountPercentage, StartDate, EndDate, Active)
VALUES
    (1, 'Pop Album Special', 10.00, '2026-09-01', '2026-12-31', 1),
    (2, 'Rock Music Offer', 15.00, '2026-09-01', '2026-12-31', 1);
GO

/* Sample sale so Dashboard and Sales Reports are not empty. */
DECLARE @SampleUserID INT;
DECLARE @SampleSaleID INT;

SELECT @SampleUserID = UserID
FROM dbo.USER_ACCOUNT
WHERE Username = 'sales1';

INSERT INTO dbo.SALE
    (SaleDateTime, UserID, Subtotal, DiscountAmount, TotalAmount)
VALUES
    (SYSDATETIME(), @SampleUserID, 7500.00, 450.00, 7050.00);

SET @SampleSaleID = SCOPE_IDENTITY();

INSERT INTO dbo.SALE_ITEM
    (SaleID, ItemID, Quantity, UnitPrice, DiscountPercentage, LineTotal)
VALUES
    (@SampleSaleID, 1, 1, 4500.00, 10.00, 4050.00),
    (@SampleSaleID, 3, 2, 1500.00, 0.00, 3000.00);

INSERT INTO dbo.RECEIPT
    (SaleID, ReceiptNumber)
VALUES
    (
        @SampleSaleID,
        'RCP-' + RIGHT('000000' + CAST(@SampleSaleID AS VARCHAR(10)), 6)
    );


/* Verification queries */
SELECT DB_NAME() AS ConnectedDatabase;

SELECT COUNT(*) AS UserCount FROM dbo.USER_ACCOUNT;
SELECT COUNT(*) AS GenreCount FROM dbo.GENRE;
SELECT COUNT(*) AS MusicItemCount FROM dbo.MUSIC_ITEM;
SELECT COUNT(*) AS PromotionCount FROM dbo.PROMOTION;
SELECT COUNT(*) AS SaleCount FROM dbo.SALE;
SELECT COUNT(*) AS SaleItemCount FROM dbo.SALE_ITEM;
SELECT COUNT(*) AS ReceiptCount FROM dbo.RECEIPT;

SELECT * FROM dbo.USER_ACCOUNT ORDER BY UserID;
SELECT * FROM dbo.GENRE ORDER BY GenreID;

SELECT
    m.ItemID,
    m.Title,
    m.Artist,
    g.GenreName,
    m.ItemFormat,
    m.Price
FROM dbo.MUSIC_ITEM AS m
INNER JOIN dbo.GENRE AS g
    ON m.GenreID = g.GenreID
ORDER BY m.ItemID;

SELECT * FROM dbo.PROMOTION ORDER BY PromotionID;
SELECT * FROM dbo.SALE ORDER BY SaleDateTime DESC;
SELECT * FROM dbo.SALE_ITEM ORDER BY SaleItemID;
SELECT * FROM dbo.RECEIPT ORDER BY ReceiptID;

