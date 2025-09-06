SELECT E.first_name || '-' || E.last_name AS "F AND L",
       NVL(D.department_name, 'X') as "Department",
       M.first_name as "Manager",
       TO_CHAR(e.salary, '$999,999.99') AS "salary",
       MM.first_name as "Manager's manager",
       TO_CHAR(MM.salary) as "Manager's manager's salary"
FROM EMPLOYEES E
JOIN EMPLOYEES M
     ON E.manager_id = M.employee_id
JOIN EMPLOYEES MM
     on M.manager_id = MM.employee_id
JOIN DEPARTMENTS D
     on E.department_id = D.DEPARTMENT_ID
WHERE
     UPPER(MM.first_name) LIKE '%A%'
     AND MOD(MM.salary, 17) = 0
