package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete", "/report" })
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controller() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		System.out.println(action);

		if (action.equals("/main")) {
			contatos(request, response);
		} else if (action.equals("/insert")) {
			novoContato(request, response);
		} else if (action.equals("/select")) {
			listarContato(request, response);
		} else if (action.equals("/update")) {
			editarContato(request, response);
		} else if (action.equals("/delete")) {
			removerContato(request, response);
		}else if (action.equals("/report")) {
			gerarRelatorio(request, response);
		}else {
			response.sendRedirect("index.html");
		}
	}

	// Listar Contatos
	protected void contatos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// criando um objeto que receberá os dados JavaBeans
		ArrayList<JavaBeans> lista = dao.listarContatos();
		// encaminhar a lista para agenda.jsp
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
		rd.forward(request, response);
	}

	// Novo contato
	protected void novoContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// setar variaveis javabeans
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		// invocar o metodo inserirContato passando o objeto contato
		dao.inserirContato(contato);
		// redirecionar para o documento agenda.jsp
		response.sendRedirect("main");
	}

	// Editar contato
	protected void listarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idcon = request.getParameter("idcon");
		// setar variavel JavaBeans
		contato.setIdcon(idcon);
		// Executar o método selecionarContato (DAO)
		dao.selecionarContato(contato);
		// teste de recebimento
		System.out.println(contato.getIdcon());
		System.out.println(contato.getNome());
		System.out.println(contato.getFone());
		System.out.println(contato.getEmail());
		// setar os atributos do formulário com o conteúdp JavaBeans
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());
		// encaminhar ao editar.jsp
		RequestDispatcher rd = request.getRequestDispatcher("editar.jsp");
		rd.forward(request, response);
	}
	
	protected void editarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// teste de recebimento
		System.out.println(request.getParameter("idcon"));
		System.out.println(request.getParameter("nome"));
		System.out.println(request.getParameter("fone"));
		System.out.println(request.getParameter("email"));
		//setar as variaveis JavaBeans
		contato.setIdcon(request.getParameter("idcon"));
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		
		//executar metodo alteraContato
		dao.alterarContato(contato);	
		//redirecionar para agenda.jsp
		response.sendRedirect("main");
	}
	
	protected void removerContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idcon = request.getParameter("idcon");
		contato.setIdcon(idcon);
		dao.deletarContato(contato);
		response.sendRedirect("main");
	}
	
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Document documento = new Document();
		try {
			response.setContentType("apllication/pdf");
			response.addHeader("Content-Disposition", "inline; filename=" + "contatos.pdf");
			PdfWriter.getInstance(documento, response.getOutputStream());
			documento.open();
			documento.add(new Paragraph("Lista de contatos:"));
			documento.add(new Paragraph(" "));
			PdfPTable tabela = new PdfPTable(3);
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Fone"));
			PdfPCell col3 = new PdfPCell(new Paragraph("E-mail"));
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);
			ArrayList<JavaBeans> lista = dao.listarContatos();
			for(int i = 0; i < lista.size(); i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getFone());
				tabela.addCell(lista.get(i).getEmail());
			}
			documento.add(tabela);
			documento.close();
		} catch (Exception e) {
			System.out.println(e);
			documento.close();
		}
	}
}
