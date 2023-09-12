/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parcialt1.arep.reflective;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ChatGPT {
    
   
    public ArrayList<String> getResponse(String command, String key) {
        if (Objects.equals(command, "Class")) {
            return getClass(key);
        } else {
            return new ArrayList<>();
        }
    }
    
    /**
     * 1. Class([class name]): Retorna una lista de campos declarados y métodos declarados
     * @param name
     * @return Retorna una lista de campos declarados y métodos declarados
     */
    public ArrayList<String> getClass(String name) {
        ArrayList<String> response = new ArrayList<>();
        try {
            Class c = Class.forName(name);
            for (Method m: c.getMethods()) {
                response.add(m.toString());
            }
            for (Method m: c.getDeclaredMethods()) {
                response.add(m.toString());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        }
        return response;
    }

    /**
     * 2. invoke([class name],[method name]): 
     * @param classname
     * @param method
     * @return retorna el resultado de la invocación del método.  Ejemplo: invoke(java.lang.System, getenv).
     */
    public ArrayList<String> getInvoke(String classname, String method) {
        ArrayList<String> response = new ArrayList<>();
        try {
            Class c = Class.forName(classname);
            Method m = c.getDeclaredMethod(method,  null);
            response.add(m.invoke(null).toString());
        } catch (ClassNotFoundException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    /**
     * 3. unaryInvoke([class name],[method name],[paramtype],[param value]): 
     * @param classname
     * @param method
     * @param type
     * @param param
     * @return retorna el resultado de la invocación del método. paramtype = int | double | String.
     */
    public ArrayList<String> getUnaryInvoke(String classname, String method, String type, String param) {
        ArrayList<String> response = new ArrayList<>();
        try {
            Class c = Class.forName(classname);
            if (Objects.equals(type, "String")) {
                Class[] clas = new Class[]{String.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, param).toString());
            } else if (Objects.equals(type, "int")) {
                Class[] clas = new Class[]{int.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, Integer.parseInt(param)).toString());
            } else if (Objects.equals(type, "double")) {
                Class[] clas = new Class[]{double.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, Double.parseDouble(param)).toString());
            } else {
                response.add("Solo funciona con Parametros de tipo int, String y Double");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    /**
     * 4. binaryInvoke([class name],[method name],[paramtype 1],[param value], [paramtype 1],[param value],): 
     * retorna 
     * @param classname
     * @param method
     * @param type1
     * @param param1
     * @param type2
     * @param param2
     * @return el resultado de la invocación del método. paramtype = int | double | String.
     */
    public ArrayList<String> getBinaryInvoke(String classname, String method, String type1, String param1, String type2, String param2) {
        ArrayList<String> response = new ArrayList<>();
        try {
            Class c = Class.forName(classname);
            if (Objects.equals(type1, "String")) {
                Class[] clas = new Class[]{String.class, String.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, param1, param2).toString());
            } else if (Objects.equals(type1, "int")) {
                Class[] clas = new Class[]{int.class, int.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, Integer.parseInt(param1),Integer.parseInt(param2)).toString());
            } else if (Objects.equals(type1, "double")) {
                Class[] clas = new Class[]{double.class, double.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, Double.parseDouble(param1), Double.parseDouble(param2)).toString());
            } else {
                response.add("Solo funciona con Parametros de tipo int y String y Double");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
