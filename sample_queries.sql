select TITLE from GENERICMOVIE AS M, GENERICTAG as T, MOVIES_TAGS as MT where M.ID = MT.FK_MOVIE and T.ID = MT.FK_TAG AND (T.KEY='Comedie')
INTERSECT
select TITLE from GENERICMOVIE AS M, GENERICTAG as T, MOVIES_TAGS as MT where M.ID = MT.FK_MOVIE and T.ID = MT.FK_TAG AND (T.KEY = 'Animation')

----------------------------------

