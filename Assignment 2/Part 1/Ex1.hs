module Ex1 (
  ListBag(..),
  wf,
  empty,
  singleton,
  fromList,
  isEmpty,
  mul,
  toList,
  sumBag,
  ) where

data ListBag a = LB [(a, Int)] deriving (Show, Eq)

-- wf --
wf :: Eq a => ListBag a -> Bool
wf (LB []) = True
wf (LB ((x,xs):xss)) 
  | (xs < 0) || (mul x (LB xss)) > 0 = False --check if the multiplicity is >0 and don't exist other occurrences
  | otherwise = wf (LB xss)


-- **** Constructors ****
-- empty --
empty :: ListBag a
empty = LB []


-- singleton --
singleton :: a -> ListBag a
singleton v = LB [(v,1)]


-- fromList -- 
fromList :: Eq a => [a] -> ListBag a
fromList lst = LB (createList lst)
  where createList [] = []
        createList (x:xs) = [(x, length(filter (==x) (x:xs)))] ++ createList (filter (/=x) xs) -- count the multiplicity of x and go ahead deleting x from list 


-- **** Operations ****
-- isEmpty --
isEmpty :: ListBag a -> Bool
isEmpty (LB []) = True
isEmpty (LB x) = False


-- mul --
mul :: Eq a => a -> ListBag a -> Int
mul v (LB []) = 0
mul v (LB ((x,xs):xss))
  | v == x = xs
  | otherwise = mul v (LB xss) 


-- toList -- 
toList :: ListBag a -> [a]
toList (LB x) = concat(map (\(z,y) -> replicate y z) x) -- replicate z y times and use concat for pass from list of list to list


-- sumBag --
sumBag :: Eq a => ListBag a -> ListBag a -> ListBag a
sumBag x y = fromList((toList x) ++ toList(y)) -- convert x and y in list, merge them and apply fromList operation


-- **** Examples ****
a1 = LB [(3,2),(1,1)]
a2 = LB [(2,2), (2,2)]
a3 = LB [(1,1), (3,-1)]