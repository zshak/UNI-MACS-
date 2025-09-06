select
    d.department_name,
    nvl(to_char(e.emp_cnt), 'N/A') as emp_cnt,
    nvl(to_char(j.job_cnt), 'N/A') as job_cnt,
    nvl(to_char(s.sum_sal), 'N/A') as sum_sal,
    nvl(to_char(m.sum_min_max), 'N/A') as sum_min_max,
    nvl(ph.phone_numbers, 'N/A') as phone_numbers
from
    departments d
left join (
    select
        department_id,
        count(*) as emp_cnt
    from
        employees
    group by
        department_id
) e on d.department_id = e.department_id
left join (
    select
        e.department_id,
        count(distinct e.job_id) as job_cnt
    from
        employees e
        join jobs j on e.job_id = j.job_id
    group by
        e.department_id
) j on d.department_id = j.department_id
left join
    (select
      d.department_name,
      LISTAGG(e.phone_number, ', ') within group (order by e.salary desc) as phone_numbers
    from
      departments d
      join employees e on d.department_id = e.department_id
    where
       regexp_count(d.department_name, ' ') < 2
    group by
      d.department_name) ph on d.department_name = ph.department_name
left join (
    select
        department_id,
        sum(salary) as sum_sal
    from
        employees
    group by
        department_id
) s on d.department_id = s.department_id
left join (
    select
        e.department_id,
         min(e.salary) + max(e.salary) as sum_min_max
    from
        employees e
        join jobs j on e.job_id = j.job_id
    group by
        e.department_id
) m on d.department_id = m.department_id
join locations l on d.location_id = l.location_id
join countries c on l.country_id = c.country_id
where
    length(d.department_name) - length(replace(d.department_name, ' ', '')) < 1
    and upper(c.country_name) like concat(upper(c.country_id), '%')
group by
    d.department_id, d.department_name, e.emp_cnt, j.job_cnt, s.sum_sal, m.sum_min_max, ph.phone_numbers, c.country_name
order by
    c.country_name asc,
    c.country_name desc;