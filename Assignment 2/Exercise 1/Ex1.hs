module Ex1 () where

data ListBag a = LB [(a, Int)] deriving (Show, Eq)

-- wf --
visited :: Eq t => t -> [t] -> Bool
visited x [] = False
visited x (y:ys) = x == y || visited x ys

check :: Eq a => [(a, b)] -> [a] -> Bool
check [] _ = True
check ((x,xs):xss) list = 
  if visited x list then False
  else check xss (list++[x])

wf :: Eq a => ListBag a -> Bool
wf (LB []) = True
wf (LB ((x,xs):xss)) = check ((x,xs):xss) []


-- empty --
empty :: ListBag a
empty = LB []


-- singleton --
singleton :: a -> ListBag a
singleton v = LB [(v,1)]


-- fromList -- 
insertInList :: (Eq a, Num b) => a -> [(a,b)] -> [(a,b)]
insertInList x [] = [(x,1)]
insertInList x ((y,ys):yss)
  | x == y = [(y,ys+1)] ++ yss
  | otherwise = [(y,ys)] ++ insertInList x yss

fromList :: Eq a => [a] -> ListBag a
fromList lst = LB (createList lst [])
  where createList [] accum = accum
        createList (x:xs) accum = createList xs (insertInList x accum)


-- isEmpty --
isEmpty :: Eq a => ListBag a -> Bool
isEmpty x 
  | x == (LB []) = True
  | otherwise = False


-- mul --
mul :: Eq a => a -> ListBag a -> Int
mul v (LB []) = 0
mul v (LB ((x,xs):xss))
  | v == x = xs
  | otherwise = mul v (LB xss)
  

-- toList -- 
toList :: ListBag a -> [a]
toList (LB []) = []
toList (LB ((x,xs):xss))
  | xs == 1 = [x] ++ toList (LB xss)
  | otherwise = [x] ++ toList (LB ((x,xs-1):xss))



insertTupleInList :: (Eq a, Num b) => (a,b) -> [(a,b)] -> [(a,b)]
insertTupleInList (y,ys) [] = [(y,ys)]
insertTupleInList (y,ys) ((x,xs):xss) 
  | x == y = [(x,xs+ys)] ++ xss
  | otherwise = [(x,xs)] ++ (insertTupleInList (y,ys) xss)


sumBag :: Eq a => ListBag a -> ListBag a -> ListBag a
sumBag x y = createListBag x y
  where createListBag (LB []) (LB []) = LB []
        createListBag (LB []) x = x
        createListBag x (LB []) = x
        createListBag (LB ((x,xs):xss)) (LB ((y,ys):yss)) = createListBag (LB (insertTupleInList (y,ys) ((x,xs):xss))) (LB yss)

a1 = fromList [1..10]
a2 = fromList [1..5]