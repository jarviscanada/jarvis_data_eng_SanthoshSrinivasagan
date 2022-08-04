from sqlalchemy import create_engine
import csv, sys

def find_company_info(host: str, port: str, username: str, password: str, database: str, table: str, out_file: str):
  alchemyEngine = create_engine('postgresql+psycopg2://' + username + ':' + password + '@' + host + ':' + port + '/' + database)
  alchemyEngine.connect()
  result = alchemyEngine.execute(str("SELECT * FROM " + table))

  with open(out_file, 'w', newline='') as csvfile:
    csvwriter = csv.writer(csvfile, delimiter=',')
    csvwriter.writerow(result.keys())
    for row in result:
      csvwriter.writerow(row)

if __name__ == '__main__':
  find_company_info(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5], sys.argv[6], sys.argv[7])