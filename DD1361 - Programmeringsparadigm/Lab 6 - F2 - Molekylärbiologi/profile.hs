module Profile where

import Data.List
import MolSeq

----------------------PART 2--------------------------------------

data Profile = Profile {profileMatrix :: [[(Char, Float)]] , profileType :: SeqType, profileSeqCount :: Int, profileName :: String} deriving(Show, Eq)
--data Profile = Profile {profileMatrix :: [[Float]] , profileType :: SeqType, profileSeqCount :: Int, profileName :: String} deriving(Show, Eq)

--Skapar en Profil utifr�n en lista av sekvenser.
fromMolSeqs :: [MolSeq] -> Profile
fromMolSeqs sequences  = Profile profileMatrix profileType profileSeqCount profileName where
          profileMatrix = profileToFrequency (makeProfileMatrix sequences) (length sequences)
          profileType = seqType (head sequences)
          profileSeqCount = length sequences
          profileName = seqName (head sequences)

nucleotides = "ACGT"
aminoacids = sort "ARNDCEQGHILKMFPSTWYVX"

makeProfileMatrix :: [MolSeq] -> [[(Char, Int)]]
makeProfileMatrix [] = error "Empty sequence list"
makeProfileMatrix sl = res
  where 
    t = seqType (head sl)
    n = length sl
    defaults = if (t == DNA) then
                 zip nucleotides (replicate (length nucleotides) 0) -- Rad (i) Skapar en lista av tuplar av utseendet [(A,0), (B,0)...]
               else 
                 zip aminoacids (replicate (length aminoacids) 0)   -- Rad (ii) Samma fast f�r aminosyror
    strs = map seqSequence sl                                       -- Rad (iii) H�mtar ut alla sekvenser
    tmp1 = map (map (\x -> ((head x), (length x))) . group . sort)  -- Rad (iv) Transpose - l�gger ihop f�rsta i alla sekvenser i en lista, andra i en lista, tredje osv.
               (transpose strs)                                     -- sorterar dessa och k�r group - skapar egna listor av sammanh�ngande bokst�ver.
    equalFst a b = (fst a ) == (fst b)                              -- Skapar sedan tuplar fr�n dessa med f�rsta element bokstaven och andra hur m�nga av samma bokstav.
    res = map sort (map (\l -> unionBy equalFst l defaults) tmp1)   -- EqualFst �r ett test som kollar om f�rsta elementet �r lika i tv� tuplar.
                                                                    -- P� sista raden sl�s listan med tuplarna vi skapade i f�rsta raden ihop med listan vi skapat
                                                                    -- fr�n sekvenserna p� rad 4 med villkoret att inga dubletter av t.ex. (A,0) f�r existera.

profileToFrequency :: [[(Char, Int)]] -> Int -> [[(Char, Float)]]
profileToFrequency profile seqCount = map (map (\(a,b) -> (a, fromIntegral  b / fromIntegral seqCount))) profile

--H�mtar ut rad f�r rad och anropar rowToFrequency f�r att omvandla till frekvens
--profileToFrequency :: [[(Char, Int)]] -> Int -> [[(Char, Float)]]
--profileToFrequency profile seqCount | profile == [] = []
--                           | otherwise = (rowToFrequency (head profile) seqCount) : (profileToFrequency (tail profile)) seqCount

--Anropar tupleToFrequency p� alla element i listan f�r att byta till frekvens
--rowToFrequency :: [(Char, Int)] -> Int -> [(Char, Float)]
--rowToFrequency row seqCount | row == [] = []
--                            | otherwise = tupleToFrequency (head row) seqCount  : rowToFrequency (tail row) seqCount

--Tar en tuple och totala antalet sekvenser och returnerar motsvarande tuple d�r man delat med antalet sekvenser.
--tupleToFrequency :: (Char, Int) -> Int -> (Char, Float)
--tupleToFrequency (a, b) total  = (a, fromIntegral  b / fromIntegral total)

--(map (map (\x -> snd x)) (profileMatrix prof1))

--Plockar ut matrisen ur profilerna och anropar profileDistanceMatrix f�r att ber�kna avst�ndet.
profileDistance :: Profile -> Profile -> Float
profileDistance prof1 prof2 = profileDistanceMatrix (profileMatrix prof1) (profileMatrix prof2)

--Adderar avst�ndet f�r alla rader i matrisen
profileDistanceMatrix :: [[(Char, Float)]] -> [[(Char, Float)]] -> Float
profileDistanceMatrix prof1 prof2 | prof1 == [] && prof2 == [] = 0
                           | otherwise = profileDistanceRow (head prof1) (head prof2) + profileDistanceMatrix (tail prof1) (tail prof2)

--Adderar avst�ndet f�r alla element i en rad.
profileDistanceRow :: [(Char, Float)] -> [(Char, Float)] -> Float
profileDistanceRow row1 row2 | row1 == [] && row2 == [] = 0
                      | otherwise = abs (snd (head row1) - snd (head row2)) + profileDistanceRow (tail row1) (tail row2)
