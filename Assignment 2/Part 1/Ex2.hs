import Ex1

instance Foldable ListBag where

  foldr f x (LB []) = x
  foldr f x (LB ((y,ys):yss)) = f y (foldr f x (LB yss))

  foldl f x (LB []) = x
  foldl f x (LB ((y,ys):yss)) = foldl f (f x y) (LB yss)


mapLB :: (a -> b) -> ListBag a -> ListBag b
mapLB f (LB x) = LB (map (\(z,y) -> (f z, y)) x)


instance Functor ListBag where
  fmap = mapLB

-- **** Examples ****
a1 = LB [(3,2),(1,1)]
a2 = LB [(2,2), (2,2)]
a3 = LB [(1,1), (3,-1)]