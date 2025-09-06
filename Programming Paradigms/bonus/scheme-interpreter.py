import sys
from functools import reduce
from fractions import Fraction as frac

import types
import copy
def read_as_list(file):
    result = []
    last_token_was_space = True
    last_token_was_par = False
    should_skip = False
    is_list = False
    while True:
        if (not should_skip):
            token = file.read(1)
        if not token:
            break
        if token == "'":
            is_list = True
            continue
        if last_token_was_space and token == ' ':
            last_token_was_par = False
            should_skip = False
            continue
        if last_token_was_par and token == ' ':
            should_skip = False
            continue
        if token == "'":
            continue
        if token == '\n' or token =='\t':
            should_skip = False
            continue
        if token == '(' and is_list:
            is_list = False
            result.append(token)
            result.append('l')
            last_token_was_par = True
            last_token_was_space = False
            continue
        if token == '(' or token == ')':
            should_skip = False
            result.append(token)
            last_token_was_par = True
            last_token_was_space = False
        else:
            cur_word = ''
            while token != ' ' and token != ')' and token != '(' and token != '\n':
                cur_word += token
                token = file.read(1)
                if (not token):
                    break
            if token == ')' or token == '(':
                last_token_was_par = True
            else:
                last_token_was_space = True
            should_skip = True
            result.append(cur_word)
    return result


def compress_to_list(text_list):
    stack = []
    for symbol in text_list:
        if symbol == '(':
            stack.append(symbol)
        elif symbol == ')':
            elem = stack.pop()
            array_to_add = []
            while elem != '(':
                array_to_add.append(elem)
                elem = stack.pop()
            array_to_add.reverse()
            stack.append(array_to_add)
        else:
            stack.append(symbol)
    return stack

obj = {}


def map_fn(*args):
    function = args[0]
    l = args[1]
    try:
        obj.get(function)
    except:
       return [function] * len (l) 
    try:
        if l[0] == 'l':
            res = list(map(obj[function], l[1:]))
        else:
            res = list(map(obj[function], l))
    except:
        if l[0] == 'l':
            res = list(map(function, l[1:]))
        else:
            res = list(map(function, l))
    return res

def replace_with_real_args(l, dictionary):
    if not isinstance(l,list):
        try:
            return dictionary.get(l,l)
        except:
            return l
    
    for ind,arg in enumerate(l):
        if not isinstance(arg,list):
            l[ind] = dictionary.get(arg,arg)
        else:
            l[ind] = replace_with_real_args(arg, dictionary)
            continue
    return l

def_funcs = {}

def define_fun(*args):
    if not isinstance(args[0], list):
        global def_funcs
        def_funcs[args[0]] = args[1]
        return
    
    func_name = args[0]
    to_evaluate = args[1]
    obj[func_name[0]] = lambda_fn(func_name[1:], to_evaluate)

def lambda_fn(*args):
    
    lambda_args = args[0]
    def func(*fun_args):
        real_passed_args = fun_args
        dictionary = dict(zip(lambda_args,real_passed_args))
        list_to_eval = copy.deepcopy(args[1])
        replace_with_real_args(list_to_eval, dictionary)
        val = evaluate(list_to_eval)
        list_to_eval.clear()
        return val
    return func
    
def apply_fn(*args):
    # arg_list = args[0]
    function = obj[args[0]]
    l = args[1]
    if l[0] == 'l':
        l = args[1][1:]
    
    res = reduce(function, l)
    return res

def multiply_fn(*args):
    res = 1
    for arg in args:
        res *= int(arg)        
    return res

def division_fn(*args):
    res = frac(args[0])
    for arg in args[1:]:
        res = frac(res,int(arg))        
    return res

def subtract_fn(*args):
    res = int(args[0])
    for arg in args[1:]:
        res -= int(arg)        
    return res

def sum_fn(*args):
    # print(args)
    res = 0
    for arg in args:
        res += int(arg)        
    return res

def greater_fn(*args):
    return int(args[0]) > int(args[1])

def less_fn(*args):
    return int(args[0]) < int(args[1])

def equal_fn(*args):
    return int(args[0]) == int(args[1])

def if_fn(*args):
    predicate = args[0]
    t = args[1]
    f = args[2]
    if predicate:
        return t
    else:
        return f

def cons_fn(*args):
    elem_to_add = args[0]
    if isinstance(elem_to_add, list):
        if elem_to_add[0] == 'l':
            elem_to_add = args[0][1:]
        else:
            elem_to_add = args[0]
    list_to_be_added = args[1]
    if list_to_be_added[0] == 'l':
        list_to_be_added = args[1][1:]
    list_to_be_added.insert(0, elem_to_add)
    return list_to_be_added

def car_fn(*args):
    ind = 0
    if args[0][0] == 'l':
        ind = 1
    res = args[0][ind]
    return res

def cdr_fn(*args):
    l = args[0]
    ind = 0
    if l[0] == 'l':
        ind = 1
    del l[ind]
    return l

def length_fn(*args):
    # print(args)
    l = args[0]
    if l[0] == 'l':
        return len(l) - 1
    else: return len(l)

def null_fn(*args):
    return length_fn(*args) == 0

def append_fn(*args):

    list_to_append = args[0]  #([1 2] [3 4])
    list_to_append_to = args[1]
    if list_to_append[0] == 'l' and list_to_append_to[0] == 'l':
        res = list_to_append[1:] + list_to_append_to[1:]
    elif list_to_append[0] == 'l' and list_to_append_to[0] != 'l':
        res = list_to_append[1:] + list_to_append_to
    elif list_to_append[0] != 'l' and list_to_append_to[0] == 'l':
        res = list_to_append + list_to_append_to
    else:
        res = list_to_append + list_to_append_to
    return res

def evaluate(compressed_list):
    # print(compressed_list)
    if not isinstance(compressed_list, list):
        val = def_funcs.get(compressed_list,compressed_list)
        if isinstance(val,list):
            return evaluate(val)
        return val
    
    if compressed_list[0] == 'lambda':
        return lambda_fn(*compressed_list[1:])
    
    if compressed_list[0] == 'l':
        return compressed_list
    
    if isinstance(compressed_list[0], list):
        compressed_list[0] = evaluate(compressed_list[0])
    else:
        # print(def_funcs.get(compressed_list[0], compressed_list[0]))
        compressed_list[0] = def_funcs.get(compressed_list[0], compressed_list[0])
        if isinstance(compressed_list[0], list):
            compressed_list[0] = evaluate(compressed_list[0])
        # compressed_list[0] = def_funcs.get(compressed_list[0], compressed_list[0])
    
    for ind,func_arg in enumerate(compressed_list[1:]):
        compressed_list[ind + 1] = evaluate(func_arg)
    
    if isinstance(compressed_list[0], types.FunctionType):
        return compressed_list[0](*compressed_list[1:])
    
    try:
        return obj[compressed_list[0]](*compressed_list[1:])
    except:
        return compressed_list

def init_Obj():
    global obj
    obj = {
    'map': map_fn,
    'lambda' : lambda_fn,
    'apply' : apply_fn,
    'length' : length_fn,
    'null?' : null_fn,
    '+': sum_fn,
    '-' : subtract_fn,
    '/' : division_fn,
    '*' : multiply_fn,
    'if' : if_fn,
    '>' : greater_fn,
    '<' : less_fn,
    '=' : equal_fn,
    'cons' : cons_fn,
    'car' : car_fn,
    'cdr' : cdr_fn,
    'append': append_fn,
    }

def to_scheme(dzaan_dzerskad_gaketebuli_listi_saertod_ar_aqvs_bagebi):
    if not isinstance(dzaan_dzerskad_gaketebuli_listi_saertod_ar_aqvs_bagebi,list):
        print(dzaan_dzerskad_gaketebuli_listi_saertod_ar_aqvs_bagebi, end=" ")
        return
    print('(', end="")
    for x in dzaan_dzerskad_gaketebuli_listi_saertod_ar_aqvs_bagebi:
        if isinstance(x, list):
            to_scheme(x)
        else:
            if x == dzaan_dzerskad_gaketebuli_listi_saertod_ar_aqvs_bagebi[-1]:
                print(x, end= "")
            else:
                print(x, end= " ")
    print(')', end=" ")

def interpret(compressed_list):
    for arg in compressed_list:
        # print(arg)
        if not isinstance(arg,list):
            arg = def_funcs[arg]
        if arg[0] == 'define':
            define_fun(*arg[1:])
        elif arg[0] == 'eval':
            to_scheme(evaluate(arg[1:]))
            print('\n',end="")
        else:
            to_scheme(evaluate(arg))
            print('\n',end="")

if __name__ == '__main__':
    init_Obj()
    text_file_name = sys.argv[1]
    file = open(text_file_name, 'r')
    text_list = read_as_list(file)
    compressed = compress_to_list(text_list)
    interpret(compressed)
