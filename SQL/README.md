### Part 1 : Data Modellig
#### Exercise 1 create table


```sql
CREATE TABLE cd.members
    (
       memid integer NOT NULL, 
       surname character varying(200) NOT NULL, 
       firstname character varying(200) NOT NULL, 
       address character varying(300) NOT NULL, 
       zipcode integer NOT NULL, 
       telephone character varying(20) NOT NULL, 
       recommendedby integer,
       joindate timestamp NOT NULL,
       PRIMARY KEY (memid),
       FOREIGN KEY (recommendedby) REFERENCES cd.members(memid)
    );
```


```sql
CREATE TABLE cd.bookings
    (
       facid integer NOT NULL,
       memid integer NOT NULL,
       starttime timestamp NOT NULL,
       slots integer NOT NULL,
       FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
       FOREIGN KEY (memid) REFERENCES cd.members(memid)
    );
```

```sql
CREATE TABLE cd.facilities
    (
       facid integer NOT NULL,
       name character varying(100) NOT NULL,
       membercost numeric NOT NULL,
       guestcost numeric NOT NULL,
       initialoutlay numeric NOT NULL,
       monthlymaintenance numeric NOT NULL,
       PRIMARY KEY (facid),
    );
```


### Part 2: Practice SQL Queries

#### Modify Data

###### Question 1: The club is adding a new facility - a spa. We need to add it into the facilities table 

```sql
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
VALUES 
  (9, 'Spa', 20, 30, 100000, 800);
```

###### Questions 2: Adding the spa to the facilities table again. This time, though, we want to automatically generate the value for the next facid, rather than specifying it as a constant.

```sql
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
VALUES 
  (
    (
      select 
        max(facid) 
      from 
        cd.facilities )+ 1, 'Spa', 20, 30, 100000, 800
  );
```

###### Questions 3: We made a mistake when entering the data for the second tennis court. The initial outlay was 10000 rather than 8000: you need to alter the data to fix the error.

```sql
UPDATE cd.facilities
	SET initialoutlay = 10000
	WHERE name = 'Tennis Court 2';
```

###### Questions 4: We want to alter the price of the second tennis court so that it costs 10% more than the first one. Try to do this without using constant values for the prices, so that we can reuse the statement if we want to.

```sql
UPDATE cd.facilities
	SET 
	  membercost = (
	  SELECT membercost 
	  FROM cd.facilities 
	  WHERE name = 'Tennis Court 1'
	  ) * 1.1,
	  
	  guestcost = (
	  SELECT guestcost 
	  FROM cd.facilities 
	  WHERE name = 'Tennis Court 1'
	  ) * 1.1
	 WHERE name = 'Tennis Court 2';
```

###### Questions 5: As part of a clearout of our database, we want to delete all bookings from the cd.bookings table. How can we accomplish this?

```sql
DELETE 
FROM cd.bookings;
```

###### Questions 6: We want to remove member 37, who has never made a booking, from our database. How can we achieve that?

```sql
DELETE 
FROM cd.members 
WHERE memid = 37;
```

#### Basic

###### Questions 1: How can you produce a list of facilities, with each labelled as 'cheap' or 'expensive' depending on if their monthly maintenance cost is more than $100? Return the name and monthly maintenance of the facilities in question.

```sql
SELECT name, 
	CASE WHEN (monthlymaintenance > 100) THEN
		'expensive'
	ELSE
		'cheap'
	END AS cost
	FROM cd.facilities; 
```

###### Questions 2: You, for some reason, want a combined list of all surnames and all facility names. Yes, this is a contrived example :-). Produce that list!

```sql
SELECT surname
FROM cd.members
UNION
SELECT name
FROM cd.facilities;
```

#### Join

###### Questions 1: How can you produce a list of the start times for bookings by members named 'David Farrell'?

```sql
SELECT bks.starttime
FROM (
	cd.bookings bks
	INNER JOIN cd.members mbs
	ON mbs.memid = bks.memid 
  )
WHERE
	mbs.firstname = 'David'
	AND mbs.surname = 'Farrell';
```

###### Questions 2: How can you produce a list of the start times for bookings for tennis courts, for the date '2012-09-21'? Return a list of start time and facility name pairings, ordered by the time.

```sql
SELECT bks.starttime AS start, fts.name
FROM
	cd.facilities fts
	INNER JOIN cd.bookings bks
	ON fts.facid = bks.facid
WHERE
	fts.name LIKE 'Tennis%' and
	bks.starttime >= '2012-09-21' and
	bks.starttime < '2012-09-22'
ORDER BY bks.starttime;
```

###### Questions 3: How can you output a list of all members, including the individual who recommended them (if any)? Ensure that results are ordered by (surname, firstname).

```sql
SELECT
	mbrs.firstname AS memfname,
	mbrs.surname AS memsname,
	rec.firstname AS recfname,
	rec.surname AS recsname
FROM 
  	cd.members mbrs
	LEFT OUTER JOIN cd.members rec
	ON rec.memid = mbrs.recommendedby
	
ORDER BY mbrs.surname, mbrs.firstname;
```


###### Questions 4: How can you output a list of all members who have recommended another member? Ensure that there are no duplicates in the list, and that results are ordered by (surname, firstname).

```sql
SELECT DISTINCT
	recs.firstname as firstname,
	recs.surname as surname
FROM
	cd.members mems
	inner JOIN cd.members recs
	ON mems.recommendedby = recs.memid
ORDER BY surname, firstname; 
```

###### Questions 5: How can you output a list of all members, including the individual who recommended them (if any), without using any joins? Ensure that there are no duplicates in the list, and that each firstname + surname pairing is formatted as a column and ordered.

```sql
SELECT DISTINCT
	mbrs.firstname || ' ' || mbrs.surname
		AS member,
	(SELECT
	 	rec.firstname || ' ' || rec.surname
	 		AS recommender
	 FROM
	 	cd.members rec
	 WHERE
	 	rec.memid = mbrs.recommendedby)
FROM
	cd.members mbrs;
```




#### Aggregation

###### Questions 1: Produce a count of the number of recommendations each member has made. Order by member ID.

```sql
SELECT
	recommendedby, count(*)
FROM
	cd.members
WHERE recommendedby IS NOT NULL
GROUP BY
	recommendedby
ORDER BY recommendedby;
```

###### Questions 2: Produce a list of the total number of slots booked per facility. For now, just produce an output table consisting of facility id and slots, sorted by facility id.

```sql
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY facid
ORDER BY facid;
```

###### Questions 3: Produce a list of the total number of slots booked per facility in the month of September 2012. Produce an output table consisting of facility id and slots, sorted by the number of slots.

```sql
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE
	starttime >= '2012-09-01' AND
	starttime < '2012-10-01'
GROUP BY facid
ORDER BY SUM(slots);
```


###### Questions 4: Produce a list of the total number of slots booked per facility per month in the year of 2012. Produce an output table consisting of facility id and slots, sorted by the id and month.

```sql
SELECT
	facid,
	EXTRACT(month FROM starttime) AS month,
	SUM(slots) AS "Total Slots"
FROM
	cd.bookings
WHERE
	EXTRACT(year FROM starttime) = 2012
GROUP BY
	facid, month
ORDER BY
	facid, month;

```

###### Questions 5: Find the total number of members (including guests) who have made at least one booking.

```sql
SELECT
	count(DISTINCT memid)
FROM
	cd.bookings;
```

###### Questions 6: Produce a list of each member name, id, and their first booking after September 1st 2012. Order by member ID.

```sql
SELECT
	surname,
	firstname,
	mbrs.memid,
	min(starttime)
FROM
	cd.bookings bks
	INNER JOIN cd.members mbrs
	ON bks.memid = mbrs.memid
WHERE
	starttime >= '2012-09-01'
GROUP BY
	surname, firstname, mbrs.memid
ORDER BY memid;
```


###### Questions 7: Produce a list of member names, with each row containing the total member count. Order by join date, and include guest members.

```sql
SELECT
	count(memid) over(),
	firstname,
	surname
FROM
	cd.members
ORDER BY
	joindate;
```


###### Questions 8: Produce a monotonically increasing numbered list of members (including guests), ordered by their date of joining. Remember that member IDs are not guaranteed to be sequential.

```sql
SELECT
	count(*) over(ORDER BY joindate),
	firstname,
	surname
FROM
	cd.members
ORDER BY
	joindate;
```


###### Questions 9: Output the facility id that has the highest number of slots booked. Ensure that in the event of a tie, all tieing results get output.

```sql
SELECT facid, total
FROM (
	SELECT
  		facid,
  		sum(slots) AS total,
  		rank() over (order by sum(slots) desc) rnk
    FROM cd.bookings
	GROUP BY facid
	 ) AS rnkd
WHERE rnk = 1;
```


#### String

###### Questions 1: Output the names of all members, formatted as 'Surname, Firstname'

```sql
SELECT
	surname || ', ' || firstname AS name
FROM
	cd.members;
```


###### Questions 2: Perform a case-insensitive search to find all facilities whose name begins with 'tennis'. Retrieve all columns.

```sql
SELECT *
FROM
	cd.facilities
WHERE
	upper(name) LIKE 'Tennis%'
```




###### Questions 3: You've noticed that the club's member table has telephone numbers with very inconsistent formatting. You'd like to find all the telephone numbers that contain parentheses, returning the member ID and telephone number sorted by member ID.

```sql
SELECT
	memid,
	telephone
FROM
	cd.members
WHERE
	telephone SIMILAR TO '%[()]%';
```


###### Questions 4: You'd like to produce a count of how many members you have whose surname starts with each letter of the alphabet. Sort by the letter, and don't worry about printing out a letter if the count is 0.

```sql
SELECT
	substr (mems.surname,1,1) AS letter,
	count(*) as count 
FROM
	cd.members mems
GROUP BY letter
ORDER BY letter;
```

