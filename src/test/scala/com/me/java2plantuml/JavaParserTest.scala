package com.me.java2plantuml

import org.scalatest.FunSpec
import org.scalatest.matchers.MustMatchers

/**
 *
 */
class JavaParserTest extends FunSpec with MustMatchers {

  describe("The JavaParser works if") {
    it("Parses the package") {
      val javaSource =
        """
          |package com.vocado.vm.mw.fas.dal.entity;
        """.stripMargin

      val classDefinition = new JavaParser().parse(javaSource)
      assert(classDefinition.packageName === "com.vocado.vm.mw.fas.dal.entity")
    }

    it("Parses the class") {
      val javaSource =
        """
          |public class FasStudent implements Serializable;
        """.stripMargin

      val classDefinition = new JavaParser().parse(javaSource)
      assert(classDefinition.className === "FasStudent")
    }

    it("Parses simple fields") {
      val javaSource =
        """
          |	@Id
          |	@GeneratedValue(strategy=GenerationType.AUTO)
          |	@Column(name="SCHOOL_AY_CONFIGURATION_ID", unique=true, nullable=false)
          |	private Long schoolAyConfigurationId;
          |
          |	//bi-directional many-to-one association to Configuration
          |	@Column(name="CONFIGURATION_ID", nullable=false)
          |	private String configurationId;
        """.stripMargin

      val classDefinition = new JavaParser().parse(javaSource)
      assert(classDefinition.fields === Seq(
        FieldDefinition("schoolAyConfigurationId", SimpleFieldType("Long")),
        FieldDefinition("configurationId", SimpleFieldType("String"))
      ))
    }

    it("Parses object fields") {
      val javaSource =
        """
          |	//bi-directional many-to-one association to AwardYear
          |	@ManyToOne
          |	@JoinColumn(name="AWARD_YEAR_ID", nullable=false)
          |	private AwardYear awardYear;
        """.stripMargin

      val classDefinition = new JavaParser().parse(javaSource)
      assert(classDefinition.fields === Seq(
        FieldDefinition("awardYear", ObjectFieldType("AwardYear"))
      ))
    }

    it("Parses a Set fields") {
      val javaSource =
        """
          |    //bi-directional many-to-one association to StudentProgram
          |    @OneToMany(mappedBy = "fasStudent", cascade = CascadeType.ALL)
          |    @IndexedEmbedded
          |    private Set<StudentAwardYear> studentAwardYears = newHashSet();
        """.stripMargin

      val classDefinition = new JavaParser().parse(javaSource)
      assert(classDefinition.fields === Seq(
        FieldDefinition("studentAwardYears", CompositeFieldType("Set", "StudentAwardYear"))
      ))
    }

    it("Parses a Map fields") {
      val javaSource =
        """
          |    @Transient
          |    private Map<String, StudentAidFieldDescriptor> fieldDescriptorMap;
        """.stripMargin

      val classDefinition = new JavaParser().parse(javaSource)
      assert(classDefinition.fields === Seq(
        FieldDefinition("fieldDescriptorMap", MapFieldType("Map", "String", "StudentAidFieldDescriptor"))
      ))
    }

    it("parses abstract classes") {
      val javaSource =
        """
          |public abstract class MyTest implements Serializable {
        """.stripMargin.trim

      val classDefinition = new JavaParser().parse(javaSource)
      assert(classDefinition.className === "MyTest")
    }

    it("parses classes using \\r\\n separators") {
      val javaSource =
        """
          |package com.vocado.vm.mw.fas.dal.entity;
          |public abstract class MyTest implements Serializable {
          |	private Long recordId;
          |}
        """.stripMargin.trim.replaceAll("\n", "\r\n")

      val classDefinition = new JavaParser().parse(javaSource)
      assert(classDefinition.packageName === "com.vocado.vm.mw.fas.dal.entity")
      assert(classDefinition.className === "MyTest")
      assert(classDefinition.fields === Seq(
        FieldDefinition("recordId", SimpleFieldType.Long)
      ))
    }

    it("parses static fields") {
      val javaSource =
        """
          |  private static final long serialVersionUID = -1463788785073258094L;
        """.stripMargin.trim.replaceAll("\n", "\r\n")

      val classDefinition = new JavaParser().parse(javaSource)
      assert(classDefinition.fields === Seq(
        FieldDefinition("serialVersionUID", SimpleFieldType.Long)
      ))
    }

    it("parses transient fields") {
      val javaSource =
        """
          |    @Transient
          |    private transient Long fieldDescriptorMap;
        """.stripMargin.trim.replaceAll("\n", "\r\n")

      val classDefinition = new JavaParser().parse(javaSource)
      assert(classDefinition.fields === Seq(
        FieldDefinition("fieldDescriptorMap", SimpleFieldType.Long)
      ))
    }
  }
}
