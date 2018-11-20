package edu.ksu.cis.bandera.annotation;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000 Roby Joehanes (robbyjo@cis.ksu.edu)            *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project in the SAnToS Laboratory,         *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/santos).                  *
 * It is understood that any modification not identified as such is  *
 * not covered by the preceding statement.                           *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this toolkit; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other SAnToS projects, please visit the web-site *
 *                http://www.cis.ksu.edu/santos                      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
import ca.mcgill.sable.util.*;
public abstract class AnnotationSwitch implements Switch {
public abstract void caseBlockStmtAnnotation(BlockStmtAnnotation a);
public abstract void caseBreakStmtAnnotation(BreakStmtAnnotation a);
public abstract void caseCatchAnnotation(CatchAnnotation a);
public abstract void caseClassDeclarationAnnotation(ClassDeclarationAnnotation a);
public abstract void caseConstructorDeclarationAnnotation(ConstructorDeclarationAnnotation a);
public abstract void caseContinueStmtAnnotation(ContinueStmtAnnotation a);
public abstract void caseDoWhileStmtAnnotation(DoWhileStmtAnnotation a);
public abstract void caseEmptyStmtAnnotation(EmptyStmtAnnotation a);
public abstract void caseExpStmtAnnotation(ExpStmtAnnotation a);
public abstract void caseFieldDeclarationAnnotation(FieldDeclarationAnnotation a);
public abstract void caseForStmtAnnotation(ForStmtAnnotation a);
public abstract void caseIfStmtAnnotation(IfStmtAnnotation a);
public abstract void caseInstanceInitializerAnnotation(InstanceInitializerAnnotation a);
public abstract void caseLabeledStmtAnnotation(LabeledStmtAnnotation a);
public abstract void caseLocalDeclarationStmtAnnotation(LocalDeclarationStmtAnnotation a);
public abstract void caseMethodDeclarationAnnotation(MethodDeclarationAnnotation a);
public abstract void caseReturnStmtAnnotation(ReturnStmtAnnotation a);
public abstract void caseSequentialAnnotation(SequentialAnnotation a);
public abstract void caseStaticInitializerAnnotation(StaticInitializerAnnotation a);
public abstract void caseSuperConstructorInvocationStmtAnnotation(SuperConstructorInvocationStmtAnnotation a);
public abstract void caseSwitchStmtAnnotation(SwitchStmtAnnotation a);
public abstract void caseSynchronizedStmtAnnotation(SynchronizedStmtAnnotation a);
public abstract void caseThisConstructorInvocationStmtAnnotation(ThisConstructorInvocationStmtAnnotation a);
public abstract void caseThrowStmtAnnotation(ThrowStmtAnnotation a);
public abstract void caseTryFinallyStmtAnnotation(TryFinallyStmtAnnotation a);
public abstract void caseTryStmtAnnotation(TryStmtAnnotation a);
public abstract void caseWhileStmtAnnotation(WhileStmtAnnotation a);
}
