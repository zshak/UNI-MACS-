select
    j.job_id,
    j.job_title as name,
    coalesce(to_char(odd.emp_cnt), 'n/a') as emp_cnt,
    coalesce(to_char(even.avg_sal), 'n/a') as avg_sal,
    case when max_cnt.mx_cnt = 0 then 'n/a' else to_char(max_cnt.mx_cnt) end as mx_cnt
from
    jobs j
left join (
    select
        job_id,
        count(*) as emp_cnt
    from
        employees
    where
        mod(employee_id, 2) = 1
    group by
        job_id
) odd on j.job_id = odd.job_id
left join (
    select
        job_id,
        avg(salary) as avg_sal
    from
        employees
    where
        mod(employee_id, 2) = 0
    group by
        job_id
) even on j.job_id = even.job_id
left join (
    select
        job_id,
        count(e2.employee_id) as mx_cnt
    from
        employees e2
    where
        e2.salary = (
            select
                max(salary)
            from
            employees
             where e2.job_id = job_id
        )
    group by
        job_id
) max_cnt on j.job_id = max_cnt.job_id
where
    length(j.job_id) >= 4
    and j.max_salary - j.min_salary <> (select max(j1.max_salary - j1.min_salary) from jobs j1)
order by
    case
        when j.job_id like '%it%' then 0
        else 1
    end,
    j.job_id;