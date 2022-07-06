-- Show table schema 
\d+ retail;

-- Q1: Show first 10 rows
SELECT * FROM retail LIMIT 10;

-- Q2: Check # of records
SELECT count(*) FROM retail;                    -- Ans: 1,067,371

-- Q3: number of clients
SELECT 
	count(DISTINCT customer_id) 
FROM retail;                                    -- Ans: 5,942

-- Q4: invoice date range
SELECT 
	max(invoice_date) AS "Max",					-- Ans:          max         |         min
	min(invoice_date) AS "min" 					--------------------+---------------------
FROM retail;									-- 2011-12-09 12:50:00 | 2009-12-01 07:45:00
                                                    

-- Q5: number of SKU/merchants
SELECT 
	count(DISTINCT stock_code) 
FROM retail;                                     -- Ans: 5,305

-- Q6: Calculate average invoice amount excluding invoices with a negative amount 
SELECT avg(amnt) FROM 
		( SELECT 
			sum(quantity * unit_price) AS amnt 
		  FROM retail 
		  GROUP BY invoice_no 
		  HAVING sum(quantity * unit_price) > 0
		  ) sub;                                  -- Ans: 523.3037586125399


-- Q7: Calculate total revenue
SELECT 
	sum(quantity * unit_price) 
FROM retail;									  --Ans: 19287250.481567945


-- Q8: Calculate total revenue by YYYYMM 
SELECT
	to_char(invoice_date, 'YYYYMM') AS "YYYMM",
	sum(quantity * unit_price) AS "Sum" 
FROM retail
GROUP BY 1
ORDER BY 1;














