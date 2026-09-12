package com.github.moritisimor.jomad.srcprelude

import com.github.moritisimor.jomad.interpreter.Interpreter

fun registerListFunctionsSrc(interp: Interpreter) {
    interp.doStringOrThrow("""
      (letfun foldl (f acc l)
        (do
          (letfun aux (a h t)
          (if (isunit t)
            a
            (aux (f a h) (car t) (cdr t))))
      
        (aux acc (car l) (cdr l))))
    """)

    interp.doStringOrThrow("""
      (letfun begins_with (l1 l2)
        (if (< (len l1) (len l2))
          false
          (do
            (letfun aux (l1h l1t l2h l2t)
              (if (isunit l2t)
                true
                (if (= l1h l2h)
                  (aux (car l1t) (cdr l1t) (car l2t) (cdr l2t))
                  false)))
                
            (aux (car l1) (cdr l1) (car l2) (cdr l2))))) 
    """)

    interp.doStringOrThrow("(letfun ends_with (l1 l2) (begins_with (rev l1) (rev l2)))")
    interp.doStringOrThrow("""
      (letfun list_init (n f)
        (do
          (letfun aux (acc i)
            (if (< i 0)
              acc
              (aux (cons (f i) acc) (dec i))))
          
          (aux () (dec n)))) 
    """)

    interp.doStringOrThrow("""
      (letfun map (f l)
        (do
          (letfun aux (acc h t)
            (if (isunit t)
              (rev acc)
              (aux (cons (f h) acc) (car t) (cdr t))))
        
          (aux () (car l) (cdr l)))) 
    """)

    interp.doStringOrThrow("""
      (letfun mapi (f l)
        (do
          (letfun aux (acc h t i)
            (if (isunit t)
              (rev acc)
              (aux (cons (f h i) acc) (car t) (cdr t) (inc i))))
          
          (aux () (car l) (cdr l) 0))) 
    """)

    interp.doStringOrThrow("""
      (letfun filter (f l)
        (do
          (letfun aux (acc h t)
            (if (isunit t)
              (rev acc)
              (if (f h)
                (aux (cons h acc) (car t) (cdr t))
                (aux acc (car t) (cdr t)))))
        
          (aux () (car l) (cdr l))))
    """)

    interp.doStringOrThrow("""
      (letfun rev (l)
        (do
          (letfun aux (acc h t)
            (if (isunit t)
              acc
              (aux (cons h acc) (car t) (cdr t))))
          
          (aux () (car l) (cdr l)))) 
    """)

    interp.doStringOrThrow("""
      (letfun len (l)
        (do 
          (letfun aux (acc h t)
            (if (isunit t)
              acc
              (aux (inc acc) (car t) (cdr t))))

          (aux 0 (car l) (cdr l)))) 
    """)

    interp.doStringOrThrow("""
      (letfun foreach (f l)
        (do
          (letfun aux (h t)
            (do
              (if (isunit t)
              unit
              (do
                (f h)
                (aux (car t) (cdr t))))))
          
          (aux (car l) (cdr l))))
    """)

    interp.doStringOrThrow("""
      (letfun foreachi (f l)
        (do
          (letfun aux (h t i)
            (do
              (if (isunit t)
                unit
                (do 
                  (f h i)
                  (aux (car t) (cdr t) (inc i))))))
          
          (aux (car l) (cdr l) 0))) 
    """)

    interp.doStringOrThrow("""
      (letfun nth (l idx)
        (do
          (letfun aux (h t i)
            (if (isunit t)
              (throw \"List has no such index\")
              (if (= i 0)
                h
                (aux (car t) (cdr t) (dec i)))))
            
          (aux (car l) (cdr l) idx)))
    """)

    interp.doStringOrThrow("""
      (letfun nth_unit (l idx)
        (do
          (letfun aux (h t i)
            (if (isunit t)
              unit
              (if (= i 0)
                h
                (aux (car t) (cdr t) (dec i)))))
            
          (aux (car l) (cdr l) idx)))
    """)

    interp.doStringOrThrow("""
      (letfun range (start end list)
        (do
          (letfun aux (acc h t i)
            (if (isunit t)
              (rev acc)
              (if (and (>= i start) (<= i end))
                (aux (cons h acc) (car t) (cdr t) (inc i))
                (aux acc (car t) (cdr t) (inc i)))))
        
          (aux () (car list) (cdr list) 0))) 
    """)
}
