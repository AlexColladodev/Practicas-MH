/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qap;

/**
 * Alexander Collado Rojas Y7412507N
 * Clase PAR para representar solucion en el Greedy
 */
public class Pair<T, U> {
    private final Integer primero;
    private final Integer segundo;
    
    public Pair(Integer primero, Integer segundo) {
        this.primero = primero;
        this.segundo = segundo;
    }
    
    public Integer getPrimero() {
        return primero;
    }

    public Integer getSegundo() {
        return segundo;
    }
    
}
