#in_file = open("stringentCliques.out","r") # footballCliques.out || wikiCliques.out
in_file = open("wikiCliques.out","r") # footballCliques.out || wikiCliques.out

lines = in_file.readlines()

distribution_map = {}

for line in lines:
  length = len(line.split(" "))
  if length in distribution_map:
    distribution_map[length] = distribution_map[length] + 1
  else:
    distribution_map[length] = 1

for key in sorted(distribution_map.iteritems()):
  print(key)
