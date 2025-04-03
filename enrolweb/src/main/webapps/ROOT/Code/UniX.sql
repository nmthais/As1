DROP DATABASE UniX; 

CREATE DATABASE UniX;

USE UniX;

/* Creating Student table */
CREATE TABLE student (
stdNo			CHAR(5)		PRIMARY KEY,
lastname		VARCHAR(50),
givenNames		VARCHAR(50),
passwordHash	CHAR(128) NOT NULL,
passwordSalt 	DOUBLE);

/* Inserting sample data to Student table */
insert into student (stdNo, lastname, givenNames, passwordHash) values ('c1234', 'Wang', 'Peter', 'password');
insert into student (stdNo, lastname, givenNames, passwordHash) values ('c0002', 'Inglet', 'Robert', 'myPassword');
insert into student (stdNo, lastname, givenNames, passwordHash) values ('c0003', 'Kent', 'Mary', '12345Pass');
insert into student (stdNo, lastname, givenNames, passwordHash) values ('c0004', 'Singh', 'Virat', 'MyPet123');
insert into student (stdNo, lastname, givenNames, passwordHash) values ('cs', 'Minh Thai', 'Nguyen', 's');

/* Creating Course table */
CREATE TABLE Course (
courseID	CHAR(8)		PRIMARY KEY,
cName		VARCHAR(25)	UNIQUE	NOT NULL,
credits		INT		CHECK (credits BETWEEN 0 AND 200) DEFAULT 20);

/* Inserting sample data to Course table */
INSERT INTO Course VALUES ('COMP1140', 'Database Management', 10);
INSERT INTO Course VALUES ('SENG1110', 'Programming', 10);
INSERT INTO Course VALUES ('SENG1050', 'Web Technologies', 40);
INSERT INTO Course VALUES ('SENG2050', 'Web Engineering', 10);
INSERT INTO Course VALUES ('INFT2031', 'Systems and Network Admin', 10);
INSERT INTO Course VALUES ('INFT3050', 'Web Programming', 10);
INSERT INTO Course VALUES ('SENG4500', 'Distributed Computing', 10);
INSERT INTO Course VALUES ('INFT3060', 'Cloud Computing', 10);

/* Creating AssumedKnowledge table */
CREATE TABLE AssumedKnowledge (
courseID			CHAR(8) REFERENCES Course(courseID),
assumedKnowledge	CHAR(8) REFERENCES Course(courseID),
PRIMARY KEY (courseId, assumedKnowledge));

/* Inserting sample data to AssumedKnowledge table */
INSERT INTO AssumedKnowledge (courseID, assumedKnowledge) VALUES ('SENG2050','SENG1050');
INSERT INTO AssumedKnowledge (courseID, assumedKnowledge) VALUES ('SENG2050','COMP1140');

/* Creating PrerequisiteKnowledge table */
CREATE TABLE PrerequisiteKnowledge (
courseID			CHAR(8) REFERENCES Course(courseID),
preReqKnowledge		CHAR(8) REFERENCES Course(courseID),
PRIMARY KEY (courseId, preReqKnowledge));

/* Inserting sample data to PrerequisiteKnowledge table */
INSERT INTO PrerequisiteKnowledge (courseID, preReqKnowledge) VALUES ('SENG2050', 'SENG1110');

/* Creating Semester table */
CREATE TABLE Semester (
semesterID				INTEGER		PRIMARY KEY CHECK (semesterID >= 0),
semester				INTEGER		CHECK(semester BETWEEN 0 AND 4),
year					INTEGER		CHECK(year BETWEEN 2000 AND 9999),
openForEnrolment		BIT);

/* Inserting sample data to Semester table */
INSERT INTO Semester VALUES (100, 1, 2024, 1);
INSERT INTO Semester VALUES (101, 2, 2024, 1);
INSERT INTO Semester VALUES (102, 1, 2025, 1);

/* Creating Course Offering table */
CREATE TABLE CourseOfferings (
semesterID			INTEGER REFERENCES Semester(semesterID),
courseID			CHAR(8) REFERENCES Course(courseID),
maxCapacity			INTEGER,
PRIMARY KEY (courseID, semesterID) );

/* Inserting sample data to CourseOfferings table */
INSERT INTO CourseOfferings VALUES (101, 'COMP1140', 10);
INSERT INTO CourseOfferings VALUES (102, 'SENG1110', 10);
INSERT INTO CourseOfferings VALUES (100, 'SENG2050', 10);
INSERT INTO CourseOfferings VALUES (100, 'SENG1050', 10);
INSERT INTO CourseOfferings VALUES (102, 'INFT2031', 10);
INSERT INTO CourseOfferings VALUES (102, 'INFT3050', 10);
INSERT INTO CourseOfferings VALUES (100, 'SENG4500', 10);
INSERT INTO CourseOfferings VALUES (102, 'INFT3060', 10);


/* Creating StudentCourseRegistration table */
CREATE TABLE StudentCourseRegistration (
stdNo		CHAR(5),
courseID	CHAR(8),
semesterID	INTEGER,			
grade		CHAR(2),
mark		DECIMAL(5,2),
PRIMARY KEY (stdNo, courseID, semesterID),
FOREIGN KEY(stdNo) REFERENCES Student(stdNo),
FOREIGN KEY (courseID, semesterID) REFERENCES CourseOfferings (courseID, semesterID));

/* Inserting sample data to StudentCourseRegistration table */
 INSERT INTO StudentCourseRegistration(stdNo,courseID, semesterID) VALUES ('c1234', 'SENG1110', 102);
 
CREATE TABLE Message( message VARCHAR(50) );

-- Check maxCapacity for course enrollment
DELIMITER //
CREATE TRIGGER checkCourseMaxCap
BEFORE INSERT ON StudentCourseRegistration
FOR EACH ROW
BEGIN
	DECLARE noStudentEnrolled INT;
    DECLARE maxCapacityCheck INT;
    
    -- Count how many students have already enrolled in one course
    SELECT Count(*) INTO noStudentEnrolled
    FROM StudentCourseRegistration
    WHERE courseID = NEW.courseID AND semesterID = NEW.semesterID;
    
    -- Check student max capacity
    SELECT maxCapacity INTO maxCapacityCheck
    FROM CourseOfferings
    WHERE courseID = NEW.courseID AND semesterID = NEW.semesterID;

    -- Check if the course is full
    IF noStudentEnrolled >= maxCapacityCheck THEN
		INSERT INTO Message values ("Error: Course capacity exceeded");
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Course capacity exceeded', MYSQL_ERRNO = 10003;
        
    END IF;
End //


DELIMITER //

CREATE TRIGGER checkPrerequisiteKnowledge
BEFORE INSERT ON StudentCourseRegistration
FOR EACH ROW
BEGIN
    DECLARE prerequisiteCourseID CHAR(8);
    DECLARE studentHasPrerequisite BOOLEAN;

    -- Check if there are any prerequisites
    SELECT preReqKnowledge INTO prerequisiteCourseID
    FROM PrerequisiteKnowledge
    WHERE courseID = NEW.courseID;

    IF prerequisiteCourseID IS NOT NULL THEN
        -- Check if the student has taken the prerequisite
        SELECT COUNT(*) > 0 INTO studentHasPrerequisite
        FROM StudentCourseRegistration
        WHERE stdNo = NEW.stdNo
		AND courseID = prerequisiteCourseID
		AND grade IS NOT NULL;

        IF NOT studentHasPrerequisite THEN
			INSERT INTO Message values ('Error: Prerequisite course not completed');
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Prerequisite course not completed';
        END IF;
    END IF;
END //


DELIMITER //
CREATE TRIGGER checkMaxUnits
BEFORE INSERT ON StudentCourseRegistration
FOR EACH ROW
BEGIN
	DECLARE totalUnits INT;
    
    -- Look at how many units were taken this semester
	SELECT SUM(c.credits) INTO totalUnits
    FROM StudentCourseRegistration scr 
    JOIN CourseOfferings co ON scr.courseID = co.courseID 
    AND scr.semesterID = co.semesterID
    JOIN Course c ON co.courseID = c.courseID
    WHERE scr.stdNO = NEW.stdNo AND scr.semesterID = NEW.semesterID;
    
    IF totalUnits >= 40 THEN
		INSERT INTO Message VALUES ("Error: Total units exceeds 40 for a semester");
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Total units exceeds 40 for a semester';
	END IF;
    
END//

CREATE TRIGGER checkAssumedKnowledge
BEFORE INSERT ON StudentCourseRegistration
FOR EACH ROW
BEGIN
    DECLARE assumedKnowledgeCourseID CHAR(8);
    DECLARE studentHasAssumedKnowledge BOOLEAN;

    -- Check if there are any prerequisites
    SELECT assumedKnowledge INTO assumedKnowledgeCourseID
    FROM AssumedKnowledge
    WHERE courseID = NEW.courseID;

    IF assumedKnowledgeCourseID IS NOT NULL THEN
        -- Check if the student has taken the prerequisite
        SELECT COUNT(*) > 0 INTO studentHasAssumedKnowledge
        FROM StudentCourseRegistration
        WHERE stdNo = NEW.stdNo
		AND courseID = assumedCourseID
		AND grade IS NOT NULL;

        IF NOT studentHasAssumedKnowledge THEN
            INSERT INTO Message values ("Warning: Assumed knowledge not completed");
        END IF;
    END IF;
END //

-- check
	