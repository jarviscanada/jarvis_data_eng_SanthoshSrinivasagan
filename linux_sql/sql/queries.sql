-- Group hosts by CPU number and sort by their memory size in descending order(within each cpu_number group)
SELECT cpu_number, id, total_mem FROM host_info GROUP BY cpu_number, id ORDER BY total_mem DESC;

-- Average used memory
SELECT
    host_id,
    hostname,
    date_trunc('hour', current_timestamp) + date_part('minute', current_timestamp):: int / 5 * interval '5 min' AS clock,
    AVG(((info.total_mem - usage.memory_free)*100/info.total_mem)) AS avg_used_mem_percentage
FROM
    host_usage as usage,
    host_info as info
WHERE usage.host_id = info.id GROUP BY current_timestamp, hostname, host_id;

-- Detect host failure

SELECT
    host_id,
    date_trunc('hour', current_timestamp) + date_part('minute', current_timestamp):: int / 5 * interval '5 min' AS clock
FROM
    host_usage
GROUP BY host_id HAVING COUNT(*) < 3;