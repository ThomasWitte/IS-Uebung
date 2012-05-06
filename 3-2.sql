--3-2
--i)
select teileid, farbe, bestand-reserviert from teile natural join auftragsposds where auftrnr = 15

--ii)
select teileid, farbe, status from auftragsposds natural join bestellanforderungen natural join bestellungen where auftrnr = 15

--iii)
select from bestellugnen natural join bestellanforderungen natural left outer join auftragsposds natural join auftragsds where status = 0

--iv)
select count(*) from auftragsposds natural join bestellanforderungen natural join bestellungen where auftrnr = 15 and status = 0
