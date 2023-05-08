package byos

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ByosApplicationTest(
    @Autowired
    private val graphQLService: GraphQLService
) {

    @Test
    fun `simple query`() {
        val query = """
            query {
              allBooks {
                id
                title
                publishedin
              }
            }
        """

        val result = graphQLService.executeGraphQLQuery(query)

        val expectedResult = """
            {
              "data": {
                "allBooks": {
                  "edges": [
                    {
                      "node": {
                        "id": 1,
                        "title": "1984",
                        "publishedin": 1948
                      }
                    },
                    {
                      "node": {
                        "id": 2,
                        "title": "Animal Farm",
                        "publishedin": 1945
                      }
                    },
                    {
                      "node": {
                        "id": 3,
                        "title": "O Alquimista",
                        "publishedin": 1988
                      }
                    },
                    {
                      "node": {
                        "id": 4,
                        "title": "Brida",
                        "publishedin": 1990
                      }
                    }
                  ]
                }
              }
            }
            """

        assertJsonEquals(expectedResult, result)
    }

    @Test
    fun `simple query with more depth`() {
        val query = """
            query {
              allAuthors {
                lastName
                books {
                  title
                }
              }
            }
        """

        val result = graphQLService.executeGraphQLQuery(query)

        val expectedResult = """
        {
          "data": {
            "allAuthors": {
              "edges": [
                {
                  "node": {
                    "lastName": "Orwell",
                    "books": {
                      "edges": [
                        {
                          "node": {
                            "title": "1984"
                          }
                        },
                        {
                          "node": {
                            "title": "Animal Farm"
                          }
                        }
                      ]
                    }
                  }
                },
                {
                  "node": {
                    "lastName": "Coelho",
                    "books": {
                      "edges": [
                        {
                          "node": {
                            "title": "O Alquimista"
                          }
                        },
                        {
                          "node": {
                            "title": "Brida"
                          }
                        }
                      ]
                    }
                  }
                }
              ]
            }
          }
        }
        """

        assertJsonEquals(expectedResult, result)
    }

    @Test
    fun `query returning object`() {
        val query = """
            query {
              test {
                value
              }
            }  
        """

        val result = graphQLService.executeGraphQLQuery(query)

        val expectedResult = """
            {
              "data": {
                "test": {
                  "value": "test"
                }
              }
            }
            """

        assertJsonEquals(expectedResult, result)
    }

    @Test
    fun `query returning null`() {
        val query = """
            query {
              allOrders {
                order_id
                user {
                  user_id
                }
              }
            }
        """

        val result = graphQLService.executeGraphQLQuery(query)

        val expectedResult = """
            {
              "data": {
                "allOrders": {
                  "edges": [
                    {
                      "node": {
                        "order_id": 1,
                        "user": null
                      }
                    },
                    {
                      "node": {
                        "order_id": 2,
                        "user": {
                          "user_id": 1
                        }
                      }
                    },
                    {
                      "node": {
                        "order_id": 3,
                        "user": {
                          "user_id": 1
                        }
                      }
                    },
                    {
                      "node": {
                        "order_id": 4,
                        "user": {
                          "user_id": 2
                        }
                      }
                    }
                  ]
                }
              }
            }
            """

        assertJsonEquals(expectedResult, result)
    }

    @Test
    fun `query with self-relation`() {
        /*
               A
             /   \
            B     C
           / \   /
          D   E F

         */
        val query = """
            query {
              allTrees {
                label
                parent {
                  label
                }
                children {
                  label
                }
              }
            }
        """

        val result = graphQLService.executeGraphQLQuery(query)

        val expectedResult = """
            {
              "data": {
                "allTrees": {
                  "edges": [
                    {
                      "node": {
                        "label": "A",
                        "parent": null,
                        "children": {
                            "edges": [
                                { "node": { "label": "B" } },
                                { "node": { "label": "C" } }
                            ]
                        }
                      }
                    },
                    {
                      "node": {
                        "label": "B",
                        "parent": {
                          "label": "A"
                        },
                        "children": {
                            "edges": [
                                { "node": { "label": "D" } },
                                { "node": { "label": "E" } }
                            ]
                        }
                      }
                    },
                    {
                      "node": {
                        "label": "C",
                        "parent": {
                          "label": "A"
                        },
                        "children": {
                            "edges": [
                                { "node": { "label": "F" } }
                            ]
                        }
                      }
                    },
                    {
                      "node": {
                        "label": "D",
                        "parent": {
                          "label": "B"
                        },
                        "children": {
                          "edges": []
                        }
                      }
                    },
                    {
                      "node": {
                        "label": "E",
                        "parent": {
                          "label": "B"
                        },
                        "children": {
                          "edges": []
                        }
                      }
                    },
                    {
                      "node": {
                        "label": "F",
                        "parent": {
                          "label": "C"
                        },
                        "children": {
                          "edges": []
                        }
                      }
                    }
                  ]
                }
              }
            }
            """

        assertJsonEquals(expectedResult, result)
    }

    @Test
    fun `query with alias`() {
        val query = """
            query {
              novel: allBooks {
                nid: id
                id
                writer: author{
                  id: id
                }
              }
            }
        """

        val result = graphQLService.executeGraphQLQuery(query)

        val expectedResult = """
            {
              "data": {
                "novel": {
                  "edges": [
                    {
                      "node": {
                        "nid": 1,
                        "id": 1,
                        "writer": {
                          "id": 1
                        }
                      }
                    },
                    {
                      "node": {
                        "nid": 2,
                        "id": 2,
                        "writer": {
                          "id": 1
                        }
                      }
                    },
                    {
                      "node": {
                        "nid": 3,
                        "id": 3,
                        "writer": {
                          "id": 2
                        }
                      }
                    },
                    {
                      "node": {
                        "nid": 4,
                        "id": 4,
                        "writer": {
                          "id": 2
                        }
                      }
                    }
                  ]
                }
              }
            }
            """


        assertJsonEquals(expectedResult, result)
    }

    @Test
    fun `multiple queries?`() {
        val query = """
            query {
              test {value}
              test2: test {value}
            }
        """

        val result = graphQLService.executeGraphQLQuery(query)

        val expectedResult = """
            {
              "data": {
                "test": {
                  "value": "test"
                },
                "test2": {
                  "value": "test"
                }
              }
            }
            """

        assertJsonEquals(expectedResult, result)
    }

    @Test
    fun `query with argument`() {
        val query = """
            query {
              authorById(id: 1) {
                id
                lastName
              }
            }
        """

        val result = graphQLService.executeGraphQLQuery(query)

        val expectedResult = """
            {
              "data": {
                "authorById": {
                  "id": 1,
                  "lastName": "Orwell"
                }
              }
            }
            """

        assertJsonEquals(expectedResult, result)
    }

    @Test
    fun `n to m relation two ways`() {
        val query = """
            query {
              allBookStores {
                name
                books {
                  title
                }
                b2b {
                  stock
                  book {
                    title
                  }
                }
              }
            }
        """

        val result = graphQLService.executeGraphQLQuery(query)

        val expectedResult = """
        {
          "data": {
            "allBookStores": {
              "edges": [
                {
                  "node": {
                    "name": "Orell Füssli",
                    "books": {
                      "edges": [
                        {
                          "node": {
                            "title": "1984"
                          }
                        },
                        {
                          "node": {
                            "title": "Animal Farm"
                          }
                        },
                        {
                          "node": {
                            "title": "O Alquimista"
                          }
                        }
                      ]
                    },
                    "b2b": {
                      "edges": [
                        {
                          "node": {
                            "stock": 10,
                            "book": {
                              "title": "1984"
                            }
                          }
                        },
                        {
                          "node": {
                            "stock": 10,
                            "book": {
                              "title": "Animal Farm"
                            }
                          }
                        },
                        {
                          "node": {
                            "stock": 10,
                            "book": {
                              "title": "O Alquimista"
                            }
                          }
                        }
                      ]
                    }
                  }
                },
                {
                  "node": {
                    "name": "Ex Libris",
                    "books": {
                      "edges": [
                        {
                          "node": {
                            "title": "1984"
                          }
                        },
                        {
                          "node": {
                            "title": "O Alquimista"
                          }
                        }
                      ]
                    },
                    "b2b": {
                      "edges": [
                        {
                          "node": {
                            "stock": 1,
                            "book": {
                              "title": "1984"
                            }
                          }
                        },
                        {
                          "node": {
                            "stock": 2,
                            "book": {
                              "title": "O Alquimista"
                            }
                          }
                        }
                      ]
                    }
                  }
                },
                {
                  "node": {
                    "name": "Buchhandlung im Volkshaus",
                    "books": {
                      "edges": [
                        {
                          "node": {
                            "title": "O Alquimista"
                          }
                        }
                      ]
                    },
                    "b2b": {
                      "edges": [
                        {
                          "node": {
                            "stock": 1,
                            "book": {
                              "title": "O Alquimista"
                            }
                          }
                        }
                      ]
                    }
                  }
                }
              ]
            }
          }
        }
        """

        assertJsonEquals(expectedResult, result)
    }
}
