import csv
import json
import sys


def csv_2_json(input_csv: str, output_json: str):
  data_list = []
  with open(input_csv, mode='r') as inp:
    reader = csv.DictReader(inp)
    for row in reader:
      data_list.append(row)
  data_dict = {'table_name' : input_csv, 'data':data_list}
  with open(output_json, 'w') as outfile:
    json.dump(data_dict, outfile, indent=4)


#csv_2_json(sys.argv[1],sys.argv[2])

if __name__ == '__main__':
  csv_2_json(sys.argv[1],sys.argv[2])