SELECT DISTINCT
  SUBSTR(E.job_id, 1, INSTR(E.job_id, '_') - 1) as Job,
  (SELECT COUNT(*) from EMPLOYEES emp WHERE (SUBSTR(emp.job_id, 1, INSTR(emp.job_id, '_') - 1) = SUBSTR(E.job_id, 1, INSTR(E.job_id, '_') - 1))
  ) AS "cnt",
  case when 
    (SELECT COUNT(*) from EMPLOYEES emp WHERE (SUBSTR(emp.job_id, 1, INSTR(emp.job_id, '_') - 1) = SUBSTR(E.job_id, 1, INSTR(E.job_id, '_') - 1))) = 0 
    then 'N'
    else 'Y' end as "YN",
  (select AVG(salary)
  from EMPLOYEES Em
  WHERE (SUBSTR(Em.job_id, 1, INSTR(Em.job_id, '_') - 1) = SUBSTR(E.job_id, 1, INSTR(E.job_id, '_') - 1))) as avg_salary,
  (select Count(*)
          from EMPLOYEES Em
          WHERE (SUBSTR(Em.job_id, 1, INSTR(Em.job_id, '_') - 1) = SUBSTR(E.job_id, 1, INSTR(E.job_id, '_') - 1) 
          and (EXTRACT(Year from em.hire_date) = 2002))) as "cnt_2002"
          
from EMPLOYEES E

ORDER BY CASE WHEN Job LIKE 'IT%' THEN 0 ELSE 1 END, CASE WHEN Job LIKE 'MK%' THEN 0 ELSE 1 END, avg_salary DESC;


