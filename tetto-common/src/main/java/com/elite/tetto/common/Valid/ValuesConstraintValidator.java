/*
    Author: 刘子阳.
    Date: 2022-07-30 15:20.
    Created by IntelliJ IDEA.
*/
package com.elite.tetto.common.Valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @apiNote  自定义验证器
 */
public class ValuesConstraintValidator implements ConstraintValidator<Values, Integer> {
    
    private final Set<Integer> set = new HashSet<>();
    
    @Override
    public void initialize(Values constraintAnnotation) {
        int[] values = constraintAnnotation.values();
        for (int value : values) {
            set.add(value);
        }
    }
    
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
