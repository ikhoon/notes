val str = "abc"

for {
  fst <- str
  snd <- str.filterNot(_ == fst)
  thd <- str.filterNot(c => c == fst || c == snd)
} yield "" + fst + snd + thd