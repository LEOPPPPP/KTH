import Data.Char 

-- De tv� basfallen och det rekursiva fallet d�r man l�gger ihop de tv� senaste.
fib :: Int -> Int
fib 0 = 0
fib 1 = 1
fib n = fib (n-1) + fib (n-2)

isConst c = c `elem` ['b','c','d','f','g','h','j','k','l','m','n','p','q','r','s','t','v','w','x','z']
-- beh�vs inte isVowel c = c `elem` ['a','e','i','o','u','y']

-- Om tom, returnera tom.
-- Om f�rsta bokstaven k �r konstant, returnera kok+rovarsprak p� svansen.
-- Annars returnera bokstaven, underf�rst�tt vokal.
rovarsprak :: String -> String
rovarsprak s 
	| s == [] = []
	| isConst (head s) = (head s):'o':(head s):rovarsprak (tail s)
	| otherwise = (head s):rovarsprak (tail s)

-- Om tom, returnera tom.
-- Om f�rsta bokstaven konsonant, returnera f�rsta bokstaven och anropa svenska p� svans-svans-svans,
-- dvs p� allt utom de tre f�rsta bokst�verna.
-- Annars returnera huvudet och anropa svenska p� svansen.
svenska :: String -> String
svenska s 
	| s == [] = []
	| isConst (head s) = (head s):svenska (tail (tail (tail s)))
	| otherwise = (head s):svenska (tail s)

-- Om n�gon lista �r tom, returnera den andra.
-- Annars j�mf�r f�rsta talet i listan och returnera det st�rsta och anropa merge p� svansen p� r�tt lista
-- med den andra or�rd.
merge [] b = b
merge a [] = a
merge a b = if (head a) >= (head b)
	then (head a):(merge (tail a) b)
	else (head b):(merge a (tail b))


-- Om s �r tom finns det inga fler ord och vi returnerar det st�rsta v�rdet, current kommer inneh�lla det
-- senaste ordet vi l�st in s� vi anropar max p� den tidigare st�rsta och current.
-- Om huvudet �r en bokstav anropar vi maxcalc igen och l�gger till 1 p� current-r�knaren
-- Om det inte �r en bokstav s� �r ordet slut och vi j�mf�r current med den f�rra l�ngsta och anropar 
-- maxcalc p� svansen med antingen current eller det f�rra l�ngsta som l�ngsta och s�tter current till 0.
maxord :: String -> Int
maxord s = maxcalc s 0 0
maxcalc s longest current
	| s == [] = max longest current
	| isAlpha (head s) = maxcalc (tail s) longest (current+1)
	| current > longest = maxcalc (tail s) current 0
	| otherwise = maxcalc (tail s) longest 0


-- Varje ber�kning av n�sta fib-tal sparas i anropet. Anropet kommer k�ras tills n r�knat ner till basfallen.
-- F�r varje k�rning sparar vi det nya ihopr�knade talet i f�rsta parametern och det f�rra i andra.
ackfib :: Int -> Int
ackfib n = fibcalc n 1 0
fibcalc 0 a b = 0
fibcalc 1 a b = a
fibcalc n a b = fibcalc (n-1) (a+b) a
