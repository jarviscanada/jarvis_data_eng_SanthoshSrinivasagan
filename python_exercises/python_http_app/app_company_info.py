import requests, sys

def find_company_info(ticker: str):
  url = "https://google-finance4.p.rapidapi.com/ticker/"
  querystring = {"t":ticker,"hl":"en","gl":"US"}
  headers = {
    "X-RapidAPI-Key": "8e3733fb77mshc592554238bba60p111fafjsn5cc782c776b0",
    "X-RapidAPI-Host": "google-finance4.p.rapidapi.com"
  }
  response = requests.request("GET", url, headers=headers, params=querystring)
  json_response = response.json()

  print('Comapny name: ' + str(json_response['info']['title']))
  print('Comapny CEO: ' + str(json_response['about']['ceo']))
  print('Previous close: ' + '$' + str(json_response['price']['previous_close']))


if __name__ == '__main__':
  find_company_info(sys.argv[1])
