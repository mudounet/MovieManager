select TITLE from GENERICMOVIE AS M, GENERICTAG as T, MOVIES_TAGS as MT where M.ID = MT.FK_MOVIE and T.ID = MT.FK_TAG AND (T.KEY='Comedie')
INTERSECT
select TITLE from GENERICMOVIE AS M, GENERICTAG as T, MOVIES_TAGS as MT where M.ID = MT.FK_MOVIE and T.ID = MT.FK_TAG AND (T.KEY = 'Animation')

----------------------------------

select KEY, COUNT(*) from GENERICMOVIE AS M, GENERICTAG as T, MOVIES_TAGS as MT where M.ID = MT.FK_MOVIE and T.ID = MT.FK_TAG AND (T.KEY = 'Animation') GROUP BY KEY

----------------------------------

SELECT KEY, COUNT(*) AS FILMS_COUNT FROM GENERICTAG as T, MOVIES_TAGS as MT  WHERE T.ID = MT.FK_TAG AND MT.FK_MOVIE IN (
	SELECT M.ID from GENERICMOVIE AS M, GENERICTAG as T, MOVIES_TAGS as MT where M.ID = MT.FK_MOVIE and T.ID = MT.FK_TAG AND (T.KEY = 'Animation')) 
GROUP BY KEY ORDER BY KEY

----------------------------------

SELECT KEY, COUNT(*) AS FILMS_COUNT FROM (
	SELECT KEY, FK_MOVIE FROM GENERICTAG as T, MOVIES_TAGS as MT  WHERE T.ID = MT.FK_TAG AND MT.FK_MOVIE IN (
		SELECT M.ID from GENERICMOVIE AS M, GENERICTAG as T, MOVIES_TAGS as MT where M.ID = MT.FK_MOVIE and T.ID = MT.FK_TAG AND (T.KEY = 'Animation')) 
	GROUP BY KEY, FK_MOVIE) 
GROUP BY KEY ORDER BY KEY

----------------------------------

SELECT KEY, COUNT(*) AS FILMS_COUNT FROM GENERICTAG as T, MOVIES_TAGS as MT  WHERE T.TYPE='S' AND T.ID = MT.FK_TAG AND MT.FK_MOVIE IN (
	select M.ID from GENERICMOVIE AS M, GENERICTAG as T, MOVIES_TAGS as MT where M.ID = MT.FK_MOVIE and T.ID = MT.FK_TAG AND (T.KEY = 'Animation'))
GROUP BY KEY ORDER BY KEY

----------------------------------

SELECT TITLE from GENERICMOVIE where ID IN (
	SELECT FK_MOVIE from GENERICTAG as T, MOVIES_TAGS as MT where T.ID = MT.FK_TAG AND (T.KEY='Comedie')
	INTERSECT
	SELECT FK_MOVIE from GENERICTAG as T, MOVIES_TAGS as MT where T.ID = MT.FK_TAG AND (T.KEY='Animation')
INTERSECT
	SELECT FK_MOVIE from GENERICTAG as T, MOVIES_TAGS as MT where T.ID = MT.FK_TAG AND (T.KEY='Oscar')
)

----------------------------------

SELECT TITLE from GENERICMOVIE where ID IN (
	SELECT FK_MOVIE from MOVIES_TAGS where FK_TAG IN (SELECT ID FROM GENERICTAG where KEY='Comedie')
	INTERSECT
	SELECT FK_MOVIE from MOVIES_TAGS where FK_TAG IN (SELECT ID FROM GENERICTAG where KEY='Animation')
	INTERSECT
	SELECT FK_MOVIE from MOVIES_TAGS where FK_TAG IN (SELECT ID FROM GENERICTAG where KEY='Oscar')
)

----------------------------------

Found in : http://www.sergiy.ca/how-to-write-many-to-many-search-queries-in-mysql-and-hibernate/

SELECT a.*
FROM  GenericMovie as a
       INNER JOIN (SELECT Movies_Tags.fk_movie
                   FROM     Movies_Tags
                            INNER JOIN GenericMovie a
                              ON a.id = Movies_Tags.fk_movie
                            INNER JOIN Generictag t
                              ON t.id = Movies_Tags.fk_tag
                   WHERE    t.key IN ('Oscar','Documentaire','Comedie')
                   GROUP BY Movies_Tags.fk_movie
                   HAVING   Count(Movies_Tags.fk_tag) = 3) aa
         ON a.id = aa.fk_movie
		 
-----------------------------------

SELECT fk_movie
FROM Movies_Tags
	INNER JOIN GenericMovie m ON m.id = fk_movie
    INNER JOIN Generictag t ON t.id = fk_tag
WHERE t.key IN ('Oscar','Documentaire','Comedie')
GROUP BY fk_movie
HAVING Count(fk_tag) = 3