import re
from bs4 import BeautifulSoup


def check_file_name(file_name: str) -> bool:
    # 定义非法字符的正则表达式
    illegal_chars = r'[\\\/:\*\?\"<>\|]'

    # 定义非法关键字列表
    illegal_keywords = ["con", "prn", "aux", "nul", "com1", "com2", "com3", "com4", "com5", "com6",
                        "com7", "com8", "com9", "lpt1", "lpt2", "lpt3", "lpt4", "lpt5", "lpt6", "lpt7", "lpt8", "lpt9"]

    # 检查非法字符
    if re.search(illegal_chars, file_name):
        return False

    # 检查非法关键字
    for keyword in illegal_keywords:
        if keyword.lower() in file_name.lower():
            return False

    return True


def find_links(html: str) -> list:
    soup = BeautifulSoup(html, 'html.parser')
    links = soup.select('a[href]')
    return [link['href'] for link in links]
