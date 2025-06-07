export const boardService = {
  async getBoardMenu(token: string) {
    // Retorna um menu mock ou vazio
    return [
      {
        id: 1,
        app: 'board',
        config: '{}',
        parent: 0,
        title: 'Home',
        position: 1,
        extension: '',
        is_menu_child: 0,
        permissions: null,
        children: []
      }
    ];
  }
}; 