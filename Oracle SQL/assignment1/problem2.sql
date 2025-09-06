SELECT E.first_name || ' ' || E.last_name as "full_name",
       TRIM(TO_CHAR(E.salary, '$999,999,999.99')) as "salary",
       case WHEN (E.email is null or E.email not like '%@%')
       THEN 'INVALID MAIL' else SUBSTR(E.email, 1, INSTR(E.email, '@') - 1) end AS "mail" ,
       E.salary *  (EXTRACT(MONTH FROM SYSDATE) - 1) as "current salary",
       case WHEN E.commission_pct is NULL THEN 'No Com'
       ELse TO_CHAR(E.commission_pct) end as "commision"
From EMPLOYEES E

join JOBS J
     on E.job_id = J.job_id and J.job_title != 'IT_PROG' and J.job_title != 'PU_CLERK'

where (E.department_id = 50 or TRUNC(MONTHS_BETWEEN(SYSDATE,E.hire_date)/12) >= 5)
      and E.phone_number LIKE '515%'
      and E.salary BETWEEN 5000 AND 10000
      AND E.manager_id IS NOT NULL
      
ORDER BY E.salary DEsC, E.hire_date ASC
